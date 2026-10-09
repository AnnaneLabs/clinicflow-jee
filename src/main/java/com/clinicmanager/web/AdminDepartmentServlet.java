package com.clinicmanager.web;

import com.clinicmanager.model.Department;
import com.clinicmanager.model.Specialty;
import com.clinicmanager.repository.DepartmentRepository;
import com.clinicmanager.repository.SpecialtyRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/departments")
public class AdminDepartmentServlet extends HttpServlet {
    private DepartmentRepository departmentRepository;
    private SpecialtyRepository specialtyRepository;

    @Override
    public void init() {
        departmentRepository = new DepartmentRepository();
        specialtyRepository = new SpecialtyRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("departments", departmentRepository.findAllOrdered());
        req.setAttribute("specialties", specialtyRepository.findAllWithDepartment());
        req.getRequestDispatcher("/WEB-INF/views/admin/departments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("createDepartment".equalsIgnoreCase(action)) {
                String name = req.getParameter("name");
                if (name != null && !name.isBlank()) {
                    if (departmentRepository.existsByName(name.trim())) {
                        req.getSession().setAttribute("flashError", "Department already exists.");
                    } else {
                        Department dept = new Department(name.trim());
                        departmentRepository.save(dept);
                        req.getSession().setAttribute("flashSuccess", "Department created successfully!");
                    }
                }
            } else if ("createSpecialty".equalsIgnoreCase(action)) {
                String name = req.getParameter("name");
                String deptIdStr = req.getParameter("departmentId");
                if (name != null && !name.isBlank() && deptIdStr != null) {
                    Long deptId = Long.parseLong(deptIdStr);
                    if (specialtyRepository.existsByName(name.trim())) {
                        req.getSession().setAttribute("flashError", "Specialty already exists.");
                    } else {
                        Department dept = departmentRepository.findById(deptId).orElseThrow();
                        Specialty spec = new Specialty(name.trim());
                        spec.setDepartment(dept);
                        specialtyRepository.save(spec);
                        req.getSession().setAttribute("flashSuccess", "Specialty created successfully!");
                    }
                }
            }
        } catch (RuntimeException e) {
            req.getSession().setAttribute("flashError", "Action failed: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/departments");
    }
}
