package com.clinicmanager.repository;

import com.clinicmanager.model.Department;

import java.util.List;
import java.util.Optional;

public class DepartmentRepository extends BaseRepository<Department, Long> {

    public DepartmentRepository() {
        super(Department.class);
    }

    public Optional<Department> findByName(String name) {
        return readOnly(em -> em.createQuery(
                        "SELECT d FROM Department d WHERE d.name = :name", Department.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst());
    }

    public boolean existsByName(String name) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(d) FROM Department d WHERE d.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult() > 0);
    }

    public List<Department> findAllOrdered() {
        return readOnly(em -> em.createQuery(
                        "SELECT d FROM Department d ORDER BY d.name", Department.class)
                .getResultList());
    }

    /** Used to refuse deleting a department that still has specialties. */
    public long countSpecialties(Long departmentId) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(s) FROM Specialty s WHERE s.department.id = :id", Long.class)
                .setParameter("id", departmentId)
                .getSingleResult());
    }
}