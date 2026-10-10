package com.clinicmanager.repository;

import com.clinicmanager.model.Appointment;
import com.clinicmanager.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppointmentRepository extends BaseRepository<Appointment, Long> {

    public AppointmentRepository() {
        super(Appointment.class);
    }

    /** One appointment with patient, doctor and their accounts loaded. */
    public Optional<Appointment> findByIdWithDetails(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH a.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE a.id = :id
                """, Appointment.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** "Mes rendez-vous" for a patient: newest first, cancelled ones included (history). */
    public List<Appointment> findByPatientId(Long patientId) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH a.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE p.id = :patientId
                ORDER BY a.startTime DESC
                """, Appointment.class)
                .setParameter("patientId", patientId)
                .getResultList());
    }

    /** Doctor agenda: every status, between from (inclusive) and to (exclusive). */
    public List<Appointment> findByDoctorAndPeriod(Long doctorId, LocalDateTime from, LocalDateTime to) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH a.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                WHERE d.id = :doctorId
                  AND a.startTime >= :from
                  AND a.startTime < :to
                ORDER BY a.startTime
                """, Appointment.class)
                .setParameter("doctorId", doctorId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList());
    }

    /**
     * Appointments that still occupy time (everything except CANCELED).
     * Used by TimeSlotService to remove already-booked slots.
     */
    public List<Appointment> findActiveByDoctorAndPeriod(Long doctorId, LocalDateTime from, LocalDateTime to) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Appointment a
                WHERE a.doctor.id = :doctorId
                  AND a.status <> :canceled
                  AND a.startTime >= :from
                  AND a.startTime < :to
                ORDER BY a.startTime
                """, Appointment.class)
                .setParameter("doctorId", doctorId)
                .setParameter("canceled", AppointmentStatus.CANCELED)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList());
    }

    /**
     * Conflict check: two ranges overlap when each starts before the other ends.
     * Back-to-back appointments (end == start) do NOT overlap.
     * Pass excludeAppointmentId when rescheduling (an appointment must not conflict with itself),
     * or null for a new booking.
     */
    public boolean existsOverlapping(Long doctorId, LocalDateTime start,
                                     LocalDateTime end, Long excludeAppointmentId) {
        return readOnly(em -> em.createQuery("""
                SELECT COUNT(a) FROM Appointment a
                WHERE a.doctor.id = :doctorId
                  AND a.status <> :canceled
                  AND a.startTime < :end
                  AND a.endTime > :start
                  AND a.id <> :excludeId
                """, Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("canceled", AppointmentStatus.CANCELED)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeAppointmentId != null ? excludeAppointmentId : -1L)
                .getSingleResult() > 0);
    }

    /** All clinic appointments with patient, doctor, specialty, and department loaded. */
    public List<Appointment> findAllWithDetails() {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Appointment a
                JOIN FETCH a.patient p
                JOIN FETCH p.user
                JOIN FETCH a.doctor d
                JOIN FETCH d.user
                LEFT JOIN FETCH d.specialty s
                LEFT JOIN FETCH s.department
                ORDER BY a.startTime DESC
                """, Appointment.class)
                .getResultList());
    }
}