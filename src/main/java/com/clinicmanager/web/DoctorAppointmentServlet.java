package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.Appointment;
import com.clinicmanager.model.AppointmentStatus;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/doctor/appointments")
public class DoctorAppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = appointmentService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            req.setAttribute("error", "Doctor profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/doctor/appointments.jsp").forward(req, resp);
            return;
        }

        Doctor doctor = doctorOpt.get();
        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctor.getId());

        req.setAttribute("doctor", doctor);
        req.setAttribute("appointments", appointments);
        req.setAttribute("statuses", AppointmentStatus.values());
        req.getRequestDispatcher("/WEB-INF/views/doctor/appointments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = appointmentService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/doctor/appointments");
            return;
        }

        Doctor doctor = doctorOpt.get();

        try {
            Long appointmentId = Long.parseLong(req.getParameter("appointmentId"));
            AppointmentStatus status = AppointmentStatus.valueOf(req.getParameter("status"));

            appointmentService.updateStatus(appointmentId, doctor.getId(), status);
            req.getSession().setAttribute("success", "Appointment status updated to " + status + ".");
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/doctor/appointments");
    }
}
