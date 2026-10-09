package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.Absence;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.service.AbsenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/doctor/absence")
public class DoctorAbsenceServlet extends HttpServlet {

    private final AbsenceService absenceService = new AbsenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = absenceService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            req.setAttribute("error", "Doctor profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/doctor/absence.jsp").forward(req, resp);
            return;
        }

        Doctor doctor = doctorOpt.get();
        List<Absence> absences = absenceService.getDoctorAbsences(doctor.getId());

        req.setAttribute("doctor", doctor);
        req.setAttribute("absences", absences);
        req.getRequestDispatcher("/WEB-INF/views/doctor/absence.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = absenceService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/doctor/absence");
            return;
        }

        Doctor doctor = doctorOpt.get();
        String action = req.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long absenceId = Long.parseLong(req.getParameter("id"));
                absenceService.deleteAbsence(absenceId, doctor.getId());
                req.getSession().setAttribute("success", "Absence declaration removed successfully.");
            } else {
                LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
                LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));
                String reason = req.getParameter("reason");

                absenceService.createAbsence(doctor.getId(), startDate, endDate, reason);
                req.getSession().setAttribute("success", "Absence declaration submitted successfully.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/doctor/absence");
    }
}
