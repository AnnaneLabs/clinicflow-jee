package com.clinicmanager.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JpaUtil {

    private static final EntityManagerFactory EMF = buildFactory();

    private JpaUtil() { }

    private static EntityManagerFactory buildFactory() {
        String dbName = required("DB_NAME");
        String url = System.getenv().getOrDefault(
                "DB_URL", "jdbc:postgresql://localhost:5432/" + dbName);

        Map<String, String> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", url);
        overrides.put("jakarta.persistence.jdbc.user", required("DB_USER"));
        overrides.put("jakarta.persistence.jdbc.password", required("DB_PASSWORD"));

        return Persistence.createEntityManagerFactory("clinicPU", overrides);
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + name);
        }
        return value;
    }

    public static EntityManager createEntityManager() {
        return EMF.createEntityManager();
    }

    public static void close() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}