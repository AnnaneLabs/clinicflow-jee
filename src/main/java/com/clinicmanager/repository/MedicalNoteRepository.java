package com.clinicmanager.repository;

import com.clinicmanager.model.MedicalNote;

import java.util.List;
import java.util.Optional;

public class MedicalNoteRepository extends BaseRepository<MedicalNote, Long> {

    public MedicalNoteRepository() {
        super(MedicalNote.class);
    }

    /** One note with its appointment and doctor loaded. */
    public Optional<MedicalNote> findByIdWithDetails(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                WHERE n.id = :id
                """, MedicalNote.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** The note of an appointment, if one exists (an appointment has at most one). */
    public Optional<MedicalNote> findByAppointmentId(Long appointmentId) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                WHERE a.id = :appointmentId
                """, MedicalNote.class)
                .setParameter("appointmentId", appointmentId)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** Medical history of a patient, newest first. */
    public List<MedicalNote> findByPatientId(Long patientId) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                WHERE a.patient.id = :patientId
                ORDER BY a.startTime DESC
                """, MedicalNote.class)
                .setParameter("patientId", patientId)
                .getResultList());
    }
}