package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.Availability;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.service.AvailabilityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@WebServlet("/doctor/availability")
public class DoctorAvailabilityServlet extends HttpServlet {

    private final AvailabilityService availabilityService = new AvailabilityService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = availabilityService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            req.setAttribute("error", "Doctor profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/doctor/availability.jsp").forward(req, resp);
            return;
        }

        Doctor doctor = doctorOpt.get();
        List<Availability> availabilities = availabilityService.getDoctorAvailabilities(doctor.getId());

        req.setAttribute("doctor", doctor);
        req.setAttribute("availabilities", availabilities);
        req.setAttribute("daysOfWeek", DayOfWeek.values());
        req.getRequestDispatcher("/WEB-INF/views/doctor/availability.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = availabilityService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/doctor/availability");
            return;
        }

        Doctor doctor = doctorOpt.get();
        String action = req.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                Long availabilityId = Long.parseLong(req.getParameter("id"));
                availabilityService.deleteAvailability(availabilityId, doctor.getId());
                req.getSession().setAttribute("success", "Availability schedule block removed successfully.");
            } else {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(req.getParameter("dayOfWeek"));
                LocalTime startTime = LocalTime.parse(req.getParameter("startTime"));
                LocalTime endTime = LocalTime.parse(req.getParameter("endTime"));

                String validFromStr = req.getParameter("validFrom");
                LocalDate validFrom = (validFromStr != null && !validFromStr.isBlank())
                        ? LocalDate.parse(validFromStr) : LocalDate.now();

                String validToStr = req.getParameter("validTo");
                LocalDate validTo = (validToStr != null && !validToStr.isBlank())
                        ? LocalDate.parse(validToStr) : null;

                availabilityService.createAvailability(doctor.getId(), dayOfWeek, startTime, endTime, validFrom, validTo);
                req.getSession().setAttribute("success", "Availability schedule block added successfully.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/doctor/availability");
    }
}
