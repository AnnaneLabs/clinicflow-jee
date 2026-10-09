package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.*;
import com.clinicmanager.repository.DoctorRepository;

import com.clinicmanager.repository.SpecialtyRepository;
import com.clinicmanager.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@WebServlet("/patient/appointments")
public class PatientAppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final DoctorRepository doctorRepository = new DoctorRepository();
    private final SpecialtyRepository specialtyRepository = new SpecialtyRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Patient> patientOpt = appointmentService.getPatientByUserId(user.getId());
        if (patientOpt.isEmpty()) {
            req.setAttribute("error", "Patient profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/patient/appointments.jsp").forward(req, resp);
            return;
        }

        String action = req.getParameter("action");
        if ("getSlots".equalsIgnoreCase(action)) {
            try {
                Long doctorId = Long.parseLong(req.getParameter("doctorId"));
                LocalDate date = LocalDate.parse(req.getParameter("date"));

                List<LocalTime> slots = appointmentService.getAvailableTimeSlots(doctorId, date);
                resp.setContentType("application/json");
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < slots.size(); i++) {
                    json.append("\"").append(slots.get(i).toString()).append("\"");
                    if (i < slots.size() - 1) json.append(",");
                }
                json.append("]");
                resp.getWriter().write(json.toString());
                return;
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("[]");
                return;
            }
        }

        Patient patient = patientOpt.get();
        List<Appointment> appointments = appointmentService.getPatientAppointments(patient.getId());
        List<Specialty> specialties = specialtyRepository.findAll();
        List<Doctor> doctors = doctorRepository.findAllWithDetails();

        req.setAttribute("patient", patient);
        req.setAttribute("appointments", appointments);
        req.setAttribute("specialties", specialties);
        req.setAttribute("doctors", doctors);
        req.setAttribute("appointmentTypes", AppointmentType.values());
        req.getRequestDispatcher("/WEB-INF/views/patient/appointments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Patient> patientOpt = appointmentService.getPatientByUserId(user.getId());
        if (patientOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/patient/appointments");
            return;
        }

        Patient patient = patientOpt.get();
        String action = req.getParameter("action");

        try {
            if ("getSlots".equalsIgnoreCase(action)) {
                Long doctorId = Long.parseLong(req.getParameter("doctorId"));
                LocalDate date = LocalDate.parse(req.getParameter("date"));

                List<LocalTime> slots = appointmentService.getAvailableTimeSlots(doctorId, date);
                resp.setContentType("application/json");
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < slots.size(); i++) {
                    json.append("\"").append(slots.get(i).toString()).append("\"");
                    if (i < slots.size() - 1) json.append(",");
                }
                json.append("]");
                resp.getWriter().write(json.toString());
                return;

            } else {
                Long doctorId = Long.parseLong(req.getParameter("doctorId"));
                LocalDate date = LocalDate.parse(req.getParameter("date"));
                LocalTime time = LocalTime.parse(req.getParameter("time"));
                AppointmentType type = AppointmentType.valueOf(req.getParameter("type"));
                String reason = req.getParameter("reason");

                appointmentService.bookAppointment(patient.getId(), doctorId, date, time, type, reason);
                req.getSession().setAttribute("success", "Appointment booked successfully for " + date + " at " + time + "!");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/patient/appointments");
    }
}
