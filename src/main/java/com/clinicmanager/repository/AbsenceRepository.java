package com.clinicmanager.repository;

import com.clinicmanager.model.Absence;

import java.time.LocalDate;
import java.util.List;

public class AbsenceRepository extends BaseRepository<Absence, Long> {

    public AbsenceRepository() {
        super(Absence.class);
    }

    public List<Absence> findByDoctorId(Long doctorId) {
        return readOnly(em -> em.createQuery("""
                SELECT a FROM Absence a
                WHERE a.doctor.id = :doctorId
                ORDER BY a.startDate DESC
                """, Absence.class)
                .setParameter("doctorId", doctorId)
                .getResultList());
    }

    public boolean isDoctorAbsentOnDate(Long doctorId, LocalDate date) {
        return readOnly(em -> em.createQuery("""
                SELECT COUNT(a) FROM Absence a
                WHERE a.doctor.id = :doctorId
                  AND a.startDate <= :date
                  AND a.endDate >= :date
                """, Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getSingleResult() > 0);
    }

    public boolean existsOverlapping(Long doctorId, LocalDate start, LocalDate end, Long excludeId) {
        return readOnly(em -> em.createQuery("""
                SELECT COUNT(a) FROM Absence a
                WHERE a.doctor.id = :doctorId
                  AND a.startDate <= :end
                  AND a.endDate >= :start
                  AND a.id <> :excludeId
                """, Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("excludeId", excludeId == null ? -1L : excludeId)
                .getSingleResult() > 0);
    }
}
