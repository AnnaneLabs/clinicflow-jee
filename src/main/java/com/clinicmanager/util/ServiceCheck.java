package com.clinicmanager.util;

import com.clinicmanager.dto.DoctorDTO;
import com.clinicmanager.dto.PatientDTO;
import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.exception.DuplicateCinException;
import com.clinicmanager.exception.DuplicateEmailException;
import com.clinicmanager.exception.DuplicateMatriculeException;
import com.clinicmanager.exception.PatientNotFoundException;
import com.clinicmanager.exception.ValidationException;
import com.clinicmanager.model.BloodType;
import com.clinicmanager.model.Department;
import com.clinicmanager.model.Gender;
import com.clinicmanager.model.Role;
import com.clinicmanager.model.Specialty;
import com.clinicmanager.repository.DepartmentRepository;
import com.clinicmanager.repository.SpecialtyRepository;
import com.clinicmanager.repository.UserRepository;
import com.clinicmanager.service.AuthService;
import com.clinicmanager.service.DoctorService;
import com.clinicmanager.service.PatientService;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;

/** Temporary manual test of PatientService and DoctorService. Delete when JUnit tests exist. */
public class ServiceCheck {

    public static void main(String[] args) {
        long n = System.nanoTime();

        AuthService auth = new AuthService();
        PatientService patients = new PatientService();
        DoctorService doctors = new DoctorService();
        UserRepository users = new UserRepository();

        // test data: one department and one specialty
        Department dept = new Department("Dept " + n);
        new DepartmentRepository().save(dept);
        Specialty spec = new Specialty("Specialty " + n);
        spec.setDepartment(dept);
        new SpecialtyRepository().save(spec);

        // ---------------------------------------------------------------- patients
        UserDTO omar = auth.register("Omar", "Idrissi", "omar" + n + "@clinic.test", "0600000000", "secret1");
        check("patient account has no profile yet", !patients.hasProfile(omar.getId()));
        expect("getProfile without profile", PatientNotFoundException.class,
                () -> patients.getProfile(omar.getId()));

        String cin = "ab" + (n % 1_000_000);
        PatientDTO profile = patients.createProfile(omar.getId(), cin, LocalDate.of(1990, 5, 17),
                Gender.MALE, "Marrakesh", BloodType.O_POSITIVE);
        check("CIN is stored uppercase", profile.getCin().equals(cin.toUpperCase()));
        check("profile mixes account and patient data", profile.getFullName().equals("Omar Idrissi")
                && profile.getBloodType() == BloodType.O_POSITIVE);
        check("hasProfile is now true", patients.hasProfile(omar.getId()));

        expect("second profile for the same account", ValidationException.class,
                () -> patients.createProfile(omar.getId(), "zz" + n, null, null, null, null));

        UserDTO lina = auth.register("Lina", "Bennis", "lina" + n + "@clinic.test", null, "secret1");
        expect("duplicate CIN", DuplicateCinException.class,
                () -> patients.createProfile(lina.getId(), cin, null, null, null, null));
        expect("birth date in the future", ValidationException.class,
                () -> patients.createProfile(lina.getId(), "fut" + n, LocalDate.now().plusDays(1), null, null, null));

        PatientDTO updated = patients.updateProfile(omar.getId(), LocalDate.of(1990, 5, 17),
                Gender.MALE, "New address", BloodType.A_POSITIVE);
        check("profile update returned new values", "New address".equals(updated.getAddress())
                && updated.getBloodType() == BloodType.A_POSITIVE);
        check("profile update was saved", "New address".equals(patients.getProfile(omar.getId()).getAddress()));
        check("listAll contains the patient", patients.listAll().stream()
                .anyMatch(p -> p.getId().equals(profile.getId())));

        // ---------------------------------------------------------------- doctors
        String doctorEmail = "dr" + n + "@clinic.test";
        String matricule = "mat" + n;
        DoctorDTO doctor = doctors.create("Sara", "Alami", doctorEmail, "0611111111", "secret1",
                matricule, "Dr.", spec.getId());
        check("doctor display name", doctor.getDisplayName().equals("Dr. Sara Alami"));
        check("doctor has specialty and department names", doctor.getSpecialtyName().equals(spec.getName())
                && doctor.getDepartmentName().equals(dept.getName()));
        check("matricule is stored uppercase", doctor.getMatricule().equals(matricule.toUpperCase()));
        check("doctor can log in with role DOCTOR", auth.login(doctorEmail, "secret1").getRole() == Role.DOCTOR);

        expect("duplicate matricule", DuplicateMatriculeException.class,
                () -> doctors.create("Other", "Doctor", "other" + n + "@clinic.test", null, "secret1",
                        matricule, "Dr.", spec.getId()));
        expect("duplicate email", DuplicateEmailException.class,
                () -> doctors.create("Other", "Doctor", doctorEmail, null, "secret1",
                        "mat2" + n, "Dr.", spec.getId()));
        expect("unknown specialty", ValidationException.class,
                () -> doctors.create("Other", "Doctor", "x" + n + "@clinic.test", null, "secret1",
                        "mat3" + n, "Dr.", -1L));

        // atomicity: a title longer than the column (50) makes the DOCTOR insert fail
        // AFTER the user insert, in the same transaction. The user must be rolled back too.
        String atomicEmail = "atomic" + n + "@clinic.test";
        expect("doctor insert fails (title too long)", PersistenceException.class,
                () -> doctors.create("Atomic", "Test", atomicEmail, null, "secret1",
                        "mat4" + n, "T".repeat(60), spec.getId()));
        check("user was rolled back with the failed doctor", !users.existsByEmail(atomicEmail));

        DoctorDTO changed = doctors.update(doctor.getId(), "Sarah", "Alami", "0622222222", "Pr.", spec.getId());
        check("update returned new values", changed.getFirstName().equals("Sarah") && "Pr.".equals(changed.getTitle()));
        DoctorDTO reloaded = doctors.getById(doctor.getId());
        check("update saved BOTH the account and the profile",
                reloaded.getFirstName().equals("Sarah") && "0622222222".equals(reloaded.getPhone())
                        && "Pr.".equals(reloaded.getTitle()));

        check("listBySpecialty finds the doctor", doctors.listBySpecialty(spec.getId()).stream()
                .anyMatch(d -> d.getId().equals(doctor.getId())));
        check("listAll finds the doctor", doctors.listAll().stream()
                .anyMatch(d -> d.getId().equals(doctor.getId())));

        JpaUtil.close();
    }

    private static void check(String label, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + label);
    }

    private static void expect(String label, Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
            System.out.println("FAIL  " + label + " (no exception thrown)");
        } catch (RuntimeException e) {
            if (type.isInstance(e)) {
                System.out.println("PASS  " + label + "  ->  " + e.getMessage());
            } else {
                System.out.println("FAIL  " + label + " (got " + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            }
        }
    }
}
