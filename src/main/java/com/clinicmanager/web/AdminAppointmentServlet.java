package com.clinicmanager.web;

import com.clinicmanager.model.Appointment;
import com.clinicmanager.model.AppointmentStatus;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.repository.AppointmentRepository;
import com.clinicmanager.repository.DoctorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/admin/appointments", "/admin/appointment"})
public class AdminAppointmentServlet extends HttpServlet {

    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final DoctorRepository doctorRepository = new DoctorRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Appointment> allAppointments = appointmentRepository.findAllWithDetails();
        List<Doctor> doctors = doctorRepository.findAllWithDetails();

        String doctorIdParam = req.getParameter("doctorId");
        String statusParam = req.getParameter("status");
        String dateParam = req.getParameter("date");

        List<Appointment> filteredAppointments = allAppointments.stream().filter(app -> {
            boolean matchesDoctor = (doctorIdParam == null || doctorIdParam.isBlank()) ||
                    app.getDoctor().getId().equals(Long.parseLong(doctorIdParam));

            boolean matchesStatus = (statusParam == null || statusParam.isBlank()) ||
                    app.getStatus().name().equalsIgnoreCase(statusParam);

            boolean matchesDate = (dateParam == null || dateParam.isBlank()) ||
                    app.getStartTime().toLocalDate().equals(LocalDate.parse(dateParam));

            return matchesDoctor && matchesStatus && matchesDate;
        }).collect(Collectors.toList());

        long totalBookings = allAppointments.size();
        long plannedCount = allAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.PLANNED).count();
        long doneCount = allAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.DONE).count();
        long canceledCount = allAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELED).count();

        req.setAttribute("appointments", filteredAppointments);
        req.setAttribute("doctors", doctors);
        req.setAttribute("statuses", AppointmentStatus.values());
        req.setAttribute("totalBookings", totalBookings);
        req.setAttribute("plannedCount", plannedCount);
        req.setAttribute("doneCount", doneCount);
        req.setAttribute("canceledCount", canceledCount);

        req.setAttribute("selectedDoctorId", doctorIdParam);
        req.setAttribute("selectedStatus", statusParam);
        req.setAttribute("selectedDate", dateParam);

        req.getRequestDispatcher("/WEB-INF/views/admin/appointments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long appointmentId = Long.parseLong(req.getParameter("appointmentId"));
            AppointmentStatus newStatus = AppointmentStatus.valueOf(req.getParameter("status"));

            Appointment app = appointmentRepository.findByIdWithDetails(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

            app.setStatus(newStatus);
            appointmentRepository.update(app);
            req.getSession().setAttribute("flashSuccess", "Appointment #" + appointmentId + " status updated to " + newStatus + ".");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", "Failed to update appointment: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/appointments");
    }
}
