package com.clinicmanager.util;

import jakarta.persistence.EntityManager;

public class SchemaCheck {

    public static void main(String[] args) {
        EntityManager em = JpaUtil.createEntityManager();
        System.out.println("Schema OK");
        em.close();
        JpaUtil.close();
    }
}