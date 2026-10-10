package com.clinicmanager.service;

import com.clinicmanager.model.*;
import com.clinicmanager.repository.AppointmentRepository;
import com.clinicmanager.repository.DoctorRepository;
import com.clinicmanager.repository.MedicalNoteRepository;
import com.clinicmanager.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

public class MedicalNoteService {

    private final MedicalNoteRepository medicalNoteRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public MedicalNoteService() {
        this.medicalNoteRepository = new MedicalNoteRepository();
        this.appointmentRepository = new AppointmentRepository();
        this.doctorRepository = new DoctorRepository();
        this.patientRepository = new PatientRepository();
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    public List<MedicalNote> getDoctorNotes(Long doctorId) {
        return medicalNoteRepository.findByDoctorId(doctorId);
    }

    public List<MedicalNote> getPatientMedicalHistory(Long patientId) {
        return medicalNoteRepository.findByPatientId(patientId);
    }

    public Optional<MedicalNote> getNoteForAppointment(Long appointmentId) {
        return medicalNoteRepository.findByAppointmentId(appointmentId);
    }

    public MedicalNote saveMedicalNote(Long appointmentId, Long doctorId, String diagnosis, String content, NoteStatus status) {
        if (diagnosis == null || diagnosis.isBlank()) {
            throw new IllegalArgumentException("Diagnosis field is required.");
        }

        Appointment appointment = appointmentRepository.findByIdWithDetails(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("You are not authorized to create or edit medical notes for this appointment.");
        }

        // If appointment is not DONE, update status to DONE when clinical note is created
        if (appointment.getStatus() != AppointmentStatus.DONE) {
            appointment.setStatus(AppointmentStatus.DONE);
            appointmentRepository.update(appointment);
        }

        Optional<MedicalNote> existingOpt = medicalNoteRepository.findByAppointmentId(appointmentId);
        MedicalNote note;
        if (existingOpt.isPresent()) {
            note = existingOpt.get();
            note.setDiagnosis(diagnosis.trim());
            note.setContent(content != null ? content.trim() : "");
            note.setStatus(status != null ? status : NoteStatus.VALIDATED);
            return medicalNoteRepository.update(note);
        } else {
            note = new MedicalNote();
            note.setAppointment(appointment);
            note.setDoctor(appointment.getDoctor());
            note.setDiagnosis(diagnosis.trim());
            note.setContent(content != null ? content.trim() : "");
            note.setStatus(status != null ? status : NoteStatus.VALIDATED);
            return medicalNoteRepository.save(note);
        }
    }
}
