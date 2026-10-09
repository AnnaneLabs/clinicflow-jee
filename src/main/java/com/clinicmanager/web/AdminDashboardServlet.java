package com.clinicmanager.web;

import com.clinicmanager.model.*;
import com.clinicmanager.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private UserRepository users;
    private DoctorRepository doctors;
    private PatientRepository patients;
    private AppointmentRepository appointments;

    @Override public void init() {
        users = new UserRepository();
        doctors = new DoctorRepository();
        patients = new PatientRepository();
        appointments = new AppointmentRepository();
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            var allUsers = users.findAll();
            req.setAttribute("totalUsers", allUsers.size());
            req.setAttribute("activeUsers", allUsers.stream().filter(User::isActive).count());
            req.setAttribute("totalDoctors", doctors.findAll().size());
            req.setAttribute("totalPatients", patients.findAll().size());
            var allAppointments = appointments.findAll();
            req.setAttribute("totalAppointments", allAppointments.size());
            req.setAttribute("plannedAppointments", allAppointments.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.PLANNED).count());
            req.setAttribute("recentAppointments", allAppointments.stream()
                    .sorted((a,b) -> b.getStartTime().compareTo(a.getStartTime()))
                    .limit(6).toList());
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            getServletContext().log("Unable to load admin dashboard.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "The dashboard could not be loaded. Check the application logs.");
        }
    }
}
