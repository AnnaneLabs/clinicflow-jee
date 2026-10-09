package com.clinicmanager.service;

import com.clinicmanager.model.Absence;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.repository.AbsenceRepository;
import com.clinicmanager.repository.DoctorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final DoctorRepository doctorRepository;

    public AbsenceService() {
        this.absenceRepository = new AbsenceRepository();
        this.doctorRepository = new DoctorRepository();
    }

    public List<Absence> getDoctorAbsences(Long doctorId) {
        return absenceRepository.findByDoctorId(doctorId);
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public Absence createAbsence(Long doctorId, LocalDate startDate, LocalDate endDate, String reason) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        if (absenceRepository.existsOverlapping(doctorId, startDate, endDate, null)) {
            throw new IllegalArgumentException("An absence declaration already exists for an overlapping date range.");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found for ID: " + doctorId));

        Absence absence = new Absence();
        absence.setDoctor(doctor);
        absence.setStartDate(startDate);
        absence.setEndDate(endDate);
        absence.setReason(reason != null ? reason.trim() : "");

        return absenceRepository.save(absence);
    }

    public void deleteAbsence(Long absenceId, Long doctorId) {
        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(() -> new IllegalArgumentException("Absence record not found."));

        if (!absence.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("You are not authorized to delete this absence record.");
        }

        absenceRepository.deleteById(absenceId);
    }
}
