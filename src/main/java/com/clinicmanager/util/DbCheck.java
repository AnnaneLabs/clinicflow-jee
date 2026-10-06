package com.clinicmanager.util;

import com.clinicmanager.model.Department;
import com.clinicmanager.model.Specialty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DbCheck {

    public static void main(String[] args) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Department cardiology = new Department("Medicine");
            Specialty heart = new Specialty("Cardiology");
            cardiology.addSpecialty(heart);

            em.persist(cardiology);
            em.persist(heart);

            tx.commit();
            System.out.println("OK: saved department id=" + cardiology.getId()
                    + " and specialty id=" + heart.getId());
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
            JpaUtil.close();
        }
    }
}