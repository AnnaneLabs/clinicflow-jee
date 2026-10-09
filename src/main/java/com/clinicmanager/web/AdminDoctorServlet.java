package com.clinicmanager.web;

import com.clinicmanager.dto.DoctorDTO;
import com.clinicmanager.model.Specialty;
import com.clinicmanager.repository.SpecialtyRepository;
import com.clinicmanager.service.DoctorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/doctors")
public class AdminDoctorServlet extends HttpServlet {
    private DoctorService doctorService;
    private SpecialtyRepository specialtyRepository;

    @Override
    public void init() {
        doctorService = new DoctorService();
        specialtyRepository = new SpecialtyRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<DoctorDTO> doctors = doctorService.listAll();
            List<Specialty> specialties = specialtyRepository.findAllWithDepartment();
            req.setAttribute("doctors", doctors);
            req.setAttribute("specialties", specialties);
            req.getRequestDispatcher("/WEB-INF/views/admin/doctors.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            getServletContext().log("Error loading doctor management page", e);
            req.setAttribute("error", "Unable to load doctors list: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/doctors.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                String initialPassword = req.getParameter("password");
                String matricule = req.getParameter("matricule");
                String title = req.getParameter("title");
                Long specialtyId = Long.parseLong(req.getParameter("specialtyId"));

                doctorService.create(firstName, lastName, email, phone, initialPassword, matricule, title, specialtyId);
                req.getSession().setAttribute("flashSuccess", "Doctor account created successfully!");
            }
        } catch (RuntimeException e) {
            req.getSession().setAttribute("flashError", e.getMessage() != null ? e.getMessage() : "Failed to save doctor.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/doctors");
    }
}
