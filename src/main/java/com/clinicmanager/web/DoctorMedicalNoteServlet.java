package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.*;
import com.clinicmanager.service.AppointmentService;
import com.clinicmanager.service.MedicalNoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/doctor/medical-notes")
public class DoctorMedicalNoteServlet extends HttpServlet {

    private final MedicalNoteService medicalNoteService = new MedicalNoteService();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = medicalNoteService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            req.setAttribute("error", "Doctor profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/doctor/medical-notes.jsp").forward(req, resp);
            return;
        }

        Doctor doctor = doctorOpt.get();
        List<MedicalNote> notes = medicalNoteService.getDoctorNotes(doctor.getId());
        List<Appointment> doctorAppointments = appointmentService.getDoctorAppointments(doctor.getId());

        String apptIdParam = req.getParameter("appointmentId");
        if (apptIdParam != null && !apptIdParam.isBlank()) {
            try {
                Long apptId = Long.parseLong(apptIdParam);
                Optional<MedicalNote> noteOpt = medicalNoteService.getNoteForAppointment(apptId);
                noteOpt.ifPresent(medicalNote -> req.setAttribute("selectedNote", medicalNote));
                req.setAttribute("selectedAppointmentId", apptId);
            } catch (Exception ignored) { }
        }

        req.setAttribute("doctor", doctor);
        req.setAttribute("notes", notes);
        req.setAttribute("appointments", doctorAppointments);
        req.setAttribute("noteStatuses", NoteStatus.values());
        req.getRequestDispatcher("/WEB-INF/views/doctor/medical-notes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Doctor> doctorOpt = medicalNoteService.getDoctorByUserId(user.getId());
        if (doctorOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/doctor/medical-notes");
            return;
        }

        Doctor doctor = doctorOpt.get();

        try {
            Long appointmentId = Long.parseLong(req.getParameter("appointmentId"));
            String diagnosis = req.getParameter("diagnosis");
            String content = req.getParameter("content");
            NoteStatus status = NoteStatus.valueOf(req.getParameter("status"));

            medicalNoteService.saveMedicalNote(appointmentId, doctor.getId(), diagnosis, content, status);
            req.getSession().setAttribute("success", "Clinical consultation note saved successfully!");
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/doctor/medical-notes");
    }
}
