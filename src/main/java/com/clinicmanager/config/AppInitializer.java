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
