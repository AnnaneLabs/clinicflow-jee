package com.clinicmanager.repository;

import com.clinicmanager.model.MedicalNote;

import java.util.List;
import java.util.Optional;

public class MedicalNoteRepository extends BaseRepository<MedicalNote, Long> {

    public MedicalNoteRepository() {
        super(MedicalNote.class);
    }

    /** One note with its appointment, patient, doctor, and specialty loaded. */
    public Optional<MedicalNote> findByIdWithDetails(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE n.id = :id
                """, MedicalNote.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** The note of an appointment, if one exists. */
    public Optional<MedicalNote> findByAppointmentId(Long appointmentId) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
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
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE p.id = :patientId
                ORDER BY a.startTime DESC
                """, MedicalNote.class)
                .setParameter("patientId", patientId)
                .getResultList());
    }

    /** Medical notes created by a doctor, newest first. */
    public List<MedicalNote> findByDoctorId(Long doctorId) {
        return readOnly(em -> em.createQuery("""
                SELECT n FROM MedicalNote n
                JOIN FETCH n.appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH n.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE d.id = :doctorId
                ORDER BY a.startTime DESC
                """, MedicalNote.class)
                .setParameter("doctorId", doctorId)
                .getResultList());
    }
}