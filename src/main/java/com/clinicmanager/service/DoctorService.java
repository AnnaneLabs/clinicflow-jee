package com.clinicmanager.service;

import com.clinicmanager.dto.DoctorDTO;
import com.clinicmanager.exception.DoctorNotFoundException;
import com.clinicmanager.exception.DuplicateEmailException;
import com.clinicmanager.exception.DuplicateMatriculeException;
import com.clinicmanager.exception.ValidationException;
import com.clinicmanager.mapper.DoctorMapper;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.model.Role;
import com.clinicmanager.model.Specialty;
import com.clinicmanager.model.User;
import com.clinicmanager.repository.DoctorRepository;
import com.clinicmanager.repository.SpecialtyRepository;
import com.clinicmanager.repository.UserRepository;
import com.clinicmanager.util.PasswordHasher;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.Locale;

import static com.clinicmanager.util.InputRules.blankToNull;
import static com.clinicmanager.util.InputRules.normalizeEmail;
import static com.clinicmanager.util.InputRules.requireText;
import static com.clinicmanager.util.InputRules.validatePassword;

/** Doctor rules. A doctor is a User (role DOCTOR) plus a Doctor profile, created together. */
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorService() {
        this(new DoctorRepository(), new UserRepository(), new SpecialtyRepository());
    }

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository,
                         SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.specialtyRepository = specialtyRepository;
    }

    /** Admin creates a doctor: account and profile are saved in ONE transaction. */
    public DoctorDTO create(String firstName, String lastName, String email, String phone,
                            String initialPassword, String matricule, String title, Long specialtyId) {
        String cleanFirstName = requireText(firstName, "First name");
        String cleanLastName = requireText(lastName, "Last name");
        String cleanEmail = normalizeEmail(email);
        String cleanMatricule = requireText(matricule, "Matricule").toUpperCase(Locale.ROOT);
        validatePassword(initialPassword);

        if (userRepository.existsByEmail(cleanEmail)) {
            throw new DuplicateEmailException(cleanEmail);
        }
        if (doctorRepository.existsByMatricule(cleanMatricule)) {
            throw new DuplicateMatriculeException(cleanMatricule);
        }
        Specialty specialty = loadSpecialty(specialtyId);

        User user = new User();
        user.setFirstName(cleanFirstName);
        user.setLastName(cleanLastName);
        user.setEmail(cleanEmail);
        user.setPhone(blankToNull(phone));
        user.setPasswordHash(PasswordHasher.hash(initialPassword));
        user.setRole(Role.DOCTOR);
        user.setActive(true);

        Doctor doctor = new Doctor();
        doctor.setMatricule(cleanMatricule);
        doctor.setTitle(blankToNull(title));
        doctor.setSpecialty(specialty);

        try {
            doctorRepository.saveWithUser(user, doctor);
        } catch (PersistenceException e) {
            // A concurrent request may have taken the email or matricule after our checks.
            if (userRepository.existsByEmail(cleanEmail)) {
                throw new DuplicateEmailException(cleanEmail);
            }
            if (doctorRepository.existsByMatricule(cleanMatricule)) {
                throw new DuplicateMatriculeException(cleanMatricule);
            }
            throw e;
        }
        return DoctorMapper.toDto(doctor);
    }

    /** The matricule cannot be changed after creation. */
    public DoctorDTO update(Long doctorId, String firstName, String lastName, String phone,
                            String title, Long specialtyId) {
        Doctor doctor = doctorRepository.findByIdWithDetails(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));

        User user = doctor.getUser();
        user.setFirstName(requireText(firstName, "First name"));
        user.setLastName(requireText(lastName, "Last name"));
        user.setPhone(blankToNull(phone));

        doctor.setTitle(blankToNull(title));
        if (specialtyId != null && !specialtyId.equals(doctor.getSpecialty().getId())) {
            doctor.setSpecialty(loadSpecialty(specialtyId));
        }

        doctorRepository.updateWithUser(doctor);
        return DoctorMapper.toDto(doctor);
    }

    public DoctorDTO getById(Long doctorId) {
        return doctorRepository.findByIdWithDetails(doctorId)
                .map(DoctorMapper::toDto)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
    }

    /** The doctor profile of a logged-in doctor account. */
    public DoctorDTO getByUserId(Long userId) {
        return doctorRepository.findByUserId(userId)
                .map(DoctorMapper::toDto)
                .orElseThrow(() -> new ValidationException("This account has no doctor profile."));
    }

    public List<DoctorDTO> listAll() {
        return DoctorMapper.toDtos(doctorRepository.findAllWithDetails());
    }

    /** For the booking flow: active doctors of a specialty. */
    public List<DoctorDTO> listBySpecialty(Long specialtyId) {
        return DoctorMapper.toDtos(doctorRepository.findBySpecialtyId(specialtyId));
    }

    private Specialty loadSpecialty(Long specialtyId) {
        if (specialtyId == null) {
            throw new ValidationException("Specialty is required.");
        }
        return specialtyRepository.findByIdWithDepartment(specialtyId)
                .orElseThrow(() -> new ValidationException("Specialty not found."));
    }
}
