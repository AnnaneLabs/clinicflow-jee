package com.clinicmanager.web;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.MedicalNote;
import com.clinicmanager.model.Patient;
import com.clinicmanager.service.MedicalNoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/patient/medical-history")
public class PatientMedicalHistoryServlet extends HttpServlet {

    private final MedicalNoteService medicalNoteService = new MedicalNoteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        Optional<Patient> patientOpt = medicalNoteService.getPatientByUserId(user.getId());
        if (patientOpt.isEmpty()) {
            req.setAttribute("error", "Patient profile not found.");
            req.getRequestDispatcher("/WEB-INF/views/patient/medical-history.jsp").forward(req, resp);
            return;
        }

        Patient patient = patientOpt.get();
        List<MedicalNote> medicalHistory = medicalNoteService.getPatientMedicalHistory(patient.getId());

        req.setAttribute("patient", patient);
        req.setAttribute("medicalHistory", medicalHistory);
        req.getRequestDispatcher("/WEB-INF/views/patient/medical-history.jsp").forward(req, resp);
    }
}
