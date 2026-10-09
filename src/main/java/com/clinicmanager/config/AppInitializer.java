package com.clinicmanager.config;

import com.clinicmanager.model.*;
import com.clinicmanager.repository.*;
import com.clinicmanager.util.JpaUtil;
import com.clinicmanager.util.PasswordHasher;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            seedInitialData();
        } catch (Exception e) {
            sce.getServletContext().log("AppInitializer failed to seed initial data.", e);
        }
    }

    private void seedInitialData() {
        UserRepository userRepository = new UserRepository();
        DepartmentRepository departmentRepository = new DepartmentRepository();
        SpecialtyRepository specialtyRepository = new SpecialtyRepository();

        // 1. Seed Admin User if none exists
        boolean hasAdmin = userRepository.findAll().stream()
                .anyMatch(u -> u.getRole() == Role.ADMIN);

        if (!hasAdmin) {
            User admin = new User();
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail("admin@clinicflow.com");
            admin.setPhone("0600000000");
            admin.setPasswordHash(PasswordHasher.hash("Admin123!"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("[AppInitializer] Seeded default Admin user: admin@clinicflow.com / Admin123!");
        }

        // 2. Seed Departments & Specialties if none exist
        if (departmentRepository.findAll().isEmpty()) {
            createDepartmentWithSpecialties(departmentRepository, specialtyRepository,
                    "Medicine", List.of("Cardiology", "Dermatology", "Neurology", "General Medicine"));
            createDepartmentWithSpecialties(departmentRepository, specialtyRepository,
                    "Pediatrics", List.of("Pediatric Care", "Child Psychology"));
            createDepartmentWithSpecialties(departmentRepository, specialtyRepository,
                    "Surgery", List.of("General Surgery", "Orthopedics"));
            createDepartmentWithSpecialties(departmentRepository, specialtyRepository,
                    "Emergency", List.of("Emergency Medicine"));
            System.out.println("[AppInitializer] Seeded initial Departments & Specialties.");
        }

        // 3. Seed Doctor User if doctor@clinicflow.com does not exist
        DoctorRepository doctorRepository = new DoctorRepository();
        boolean hasDoctor = userRepository.findByEmail("doctor@clinicflow.com").isPresent();

        if (!hasDoctor) {
            Specialty cardiology = specialtyRepository.findAll().stream()
                    .filter(s -> "Cardiology".equalsIgnoreCase(s.getName()))
                    .findFirst()
                    .orElse(null);

            User doctorUser = new User();
            doctorUser.setFirstName("Sarah");
            doctorUser.setLastName("Smith");
            doctorUser.setEmail("doctor@clinicflow.com");
            doctorUser.setPhone("0611223344");
            doctorUser.setPasswordHash(PasswordHasher.hash("Doctor123!"));
            doctorUser.setRole(Role.DOCTOR);
            doctorUser.setActive(true);

            Doctor doctor = new Doctor();
            doctor.setMatricule("DOC-1001");
            doctor.setTitle("Dr.");
            doctor.setSpecialty(cardiology);

            doctorRepository.saveWithUser(doctorUser, doctor);
            System.out.println("[AppInitializer] Seeded default Doctor user: doctor@clinicflow.com / Doctor123!");
        }

        // 4. Seed Patient User if patient@clinicflow.com does not exist
        PatientRepository patientRepository = new PatientRepository();
        boolean hasPatient = userRepository.findByEmail("patient@clinicflow.com").isPresent();

        if (!hasPatient) {
            User patientUser = new User();
            patientUser.setFirstName("John");
            patientUser.setLastName("Doe");
            patientUser.setEmail("patient@clinicflow.com");
            patientUser.setPhone("0655667788");
            patientUser.setPasswordHash(PasswordHasher.hash("Patient123!"));
            patientUser.setRole(Role.PATIENT);
            patientUser.setActive(true);

            Patient patient = new Patient();
            patient.setUser(patientUser);
            patient.setCin("AB123456");
            patient.setGender(Gender.MALE);
            patient.setBloodType(BloodType.O_POSITIVE);
            patient.setAddress("123 Main Street");

            patientRepository.saveWithUser(patientUser, patient);
            System.out.println("[AppInitializer] Seeded default Patient user: patient@clinicflow.com / Patient123!");
        }
    }

    private void createDepartmentWithSpecialties(DepartmentRepository deptRepo,
                                                 SpecialtyRepository specRepo,
                                                 String deptName,
                                                 List<String> specialtyNames) {
        Department dept = new Department(deptName);
        deptRepo.save(dept);
        for (String sName : specialtyNames) {
            Specialty spec = new Specialty(sName);
            spec.setDepartment(dept);
            specRepo.save(spec);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaUtil.close();
    }
}
