package com.clinicmanager.repository;

import com.clinicmanager.model.Specialty;

import java.util.List;
import java.util.Optional;

public class SpecialtyRepository extends BaseRepository<Specialty, Long> {

    public SpecialtyRepository() {
        super(Specialty.class);
    }

    public Optional<Specialty> findByIdWithDepartment(Long id) {
        return readOnly(em -> em.createQuery("""
                SELECT s FROM Specialty s
                JOIN FETCH s.department
                WHERE s.id = :id
                """, Specialty.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst());
    }

    public boolean existsByName(String name) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(s) FROM Specialty s WHERE s.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult() > 0);
    }

    public List<Specialty> findByDepartmentId(Long departmentId) {
        return readOnly(em -> em.createQuery("""
                SELECT s FROM Specialty s
                JOIN FETCH s.department d
                WHERE d.id = :departmentId
                ORDER BY s.name
                """, Specialty.class)
                .setParameter("departmentId", departmentId)
                .getResultList());
    }

    /** For the admin list and for the first booking step (choose a specialty). */
    public List<Specialty> findAllWithDepartment() {
        return readOnly(em -> em.createQuery("""
                SELECT s FROM Specialty s
                JOIN FETCH s.department d
                ORDER BY d.name, s.name
                """, Specialty.class)
                .getResultList());
    }
}