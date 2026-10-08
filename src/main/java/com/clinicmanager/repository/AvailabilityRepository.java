package com.clinicmanager.repository;

import com.clinicmanager.model.Availability;
import com.clinicmanager.model.AvailabilityStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AvailabilityRepository extends BaseRepository<Availability, Long> {

    public AvailabilityRepository() {
        super(Availability.class);
    }

    /** Every availability of a doctor, for the "Mes disponibilites" page. */
    public List<Availability> findByDoctorId(Long doctorId) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Availability a
                WHERE a.doctor.id = :doctorId
                ORDER BY a.dayOfWeek, a.startTime
                """, Availability.class)
                .setParameter("doctorId", doctorId)
                .getResultList());
    }

    /**
     * Availabilities that apply on a given calendar date.
     * Used by TimeSlotService to generate the slots of that day.
     */
    public List<Availability> findActiveByDoctorAndDay(Long doctorId, DayOfWeek day, LocalDate date) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Availability a
                WHERE a.doctor.id = :doctorId
                  AND a.dayOfWeek = :day
                  AND a.status = :status
                  AND a.validFrom <= :date
                  AND (a.validTo IS NULL OR a.validTo >= :date)
                ORDER BY a.startTime
                """, Availability.class)
                .setParameter("doctorId", doctorId)
                .setParameter("day", day)
                .setParameter("status", AvailabilityStatus.ACTIVE)
                .setParameter("date", date)
                .getResultList());
    }

    /**
     * True if another ACTIVE block of the same doctor on the same weekday overlaps
     * the given time range. Pass excludeId when editing an existing block (null when creating).
     * Note: this compares weekday and times only; validity periods are checked in the service.
     */
    public boolean existsOverlapping(Long doctorId, DayOfWeek day, LocalTime start,
                                     LocalTime end, Long excludeId) {
        return readOnly(em -> em.createQuery("""
                SELECT COUNT(a) FROM Availability a
                WHERE a.doctor.id = :doctorId
                  AND a.dayOfWeek = :day
                  AND a.status = :status
                  AND a.startTime < :end
                  AND a.endTime > :start
                  AND a.id <> :excludeId
                """, Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("day", day)
                .setParameter("status", AvailabilityStatus.ACTIVE)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeId == null ? -1L : excludeId)
                .getSingleResult() > 0);
    }
}