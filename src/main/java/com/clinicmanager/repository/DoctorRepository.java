package com.clinicmanager.repository;

import com.clinicmanager.model.Doctor;

import java.util.List;
import java.util.Optional;

public class DoctorRepository extends BaseRepository<Doctor, Long> {

    public DoctorRepository() {
        super(Doctor.class);
    }

    public Optional<Doctor> findByIdWithDetails(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT d FROM Doctor d
                JOIN FETCH d.user
                JOIN FETCH d.specialty s
                JOIN FETCH s.department
                WHERE d.id = :id
                """, Doctor.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** Doctor profile of a given account. */
    public Optional<Doctor> findByUserId(Long userId) {
        return readOnly(em -> em.createQuery("""
                SELECT d FROM Doctor d
                JOIN FETCH d.user u
                JOIN FETCH d.specialty s
                JOIN FETCH s.department
                WHERE u.id = :userId
                """, Doctor.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst());
    }

    public Optional<Doctor> findByMatricule(String matricule) {
        return readOnly(em -> em.createQuery("""
                SELECT d FROM Doctor d
                JOIN FETCH d.user
                JOIN FETCH d.specialty s
                JOIN FETCH s.department
                WHERE d.matricule = :matricule
                """, Doctor.class)
                .setParameter("matricule", matricule)
                .getResultList()
                .stream()
                .findFirst());
    }

    public boolean existsByMatricule(String matricule) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(d) FROM Doctor d WHERE d.matricule = :matricule", Long.class)
                .setParameter("matricule", matricule)
                .getSingleResult() > 0);
    }

    /** Used by the booking flow: step "choose a doctor" after "choose a specialty". */
    public List<Doctor> findBySpecialtyId(Long specialtyId) {
        return readOnly(em -> em.createQuery("""
                SELECT d FROM Doctor d
                JOIN FETCH d.user u
                JOIN FETCH d.specialty s
                JOIN FETCH s.department
                WHERE s.id = :specialtyId
                  AND u.active = true
                ORDER BY u.lastName, u.firstName
                """, Doctor.class)
                .setParameter("specialtyId", specialtyId)
                .getResultList());
    }

    /** For the admin list. */
    public List<Doctor> findAllWithDetails() {
        return readOnly(em -> em.createQuery("""
                SELECT d FROM Doctor d
                JOIN FETCH d.user u
                JOIN FETCH d.specialty s
                JOIN FETCH s.department
                ORDER BY u.lastName, u.firstName
                """, Doctor.class)
                .getResultList());
    }
}