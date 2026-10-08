package com.clinicmanager.repository;

import com.clinicmanager.model.User;

import java.util.Optional;

public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        return readOnly(em -> em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst());
    }

    public boolean existsByEmail(String email) {
        return readOnly(em -> em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0);
    }
}