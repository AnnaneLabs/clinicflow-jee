package com.clinicmanager.repository;

import com.clinicmanager.model.Patient;

import java.util.List;
import java.util.Optional;

public class PatientRepository extends BaseRepository<Patient, Long> {

    public PatientRepository() {
        super(Patient.class);
    }

    /** Patient profile of a given account, with the user loaded. */
    public Optional<Patient> findByUserId(Long userId) {
        return readOnly(em -> em.createQuery("""
                SELECT p FROM Patient p
                JOIN FETCH p.user u
                WHERE u.id = :userId
                """, Patient.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst());
    }

    /** Same as findById, but with the user loaded (safe to read names afterwards). */
    public Optional<Patient> findByIdWithUser(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT p FROM Patient p
                JOIN FETCH p.user
                WHERE p.id = :id
                """, Patient.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    public Optional<Patient> findByCin(String cin) {
        return readOnly(em -> em.createQuery("""
                SELECT p FROM Patient p
                JOIN FETCH p.user
                WHERE p.cin = :cin
                """, Patient.class)
                .setParameter("cin", cin)
                .getResultList()
                .stream()
                .findFirst());
    }

    public boolean existsByCin(String cin) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(p) FROM Patient p WHERE p.cin = :cin", Long.class)
                .setParameter("cin", cin)
                .getSingleResult() > 0);
    }

    /** For the admin list: every patient with the account data. */
    public List<Patient> findAllWithUser() {
        return readOnly(em -> em.createQuery("""
                SELECT p FROM Patient p
                JOIN FETCH p.user u
                ORDER BY u.lastName, u.firstName
                """, Patient.class)
                .getResultList());
    }

    public Patient saveWithUser(com.clinicmanager.model.User user, Patient patient) {
        return inTransaction(em -> {
            em.persist(user);
            patient.setUser(user);
            em.persist(patient);
            return patient;
        });
    }
}