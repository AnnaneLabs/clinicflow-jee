package com.clinicmanager.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JpaUtil {

    private static final EntityManagerFactory EMF = buildFactory();

    private JpaUtil() { }

    private static EntityManagerFactory buildFactory() {
        Map<String, String> envMap = loadEnvMap();

        String dbHost = getEnvOr(envMap, "DB_HOST", "localhost");
        String dbPort = getEnvOr(envMap, "DB_PORT", "5432");
        String dbName = getEnvOr(envMap, "DB_NAME", "clinicflow");
        String dbUser = getEnvOr(envMap, "DB_USER", "clinic");
        String dbPassword = getEnvOr(envMap, "DB_PASSWORD", "clinic");

        String defaultUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
        String url = getEnvOr(envMap, "DB_URL", defaultUrl);

        Map<String, String> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", url);
        overrides.put("jakarta.persistence.jdbc.user", dbUser);
        overrides.put("jakarta.persistence.jdbc.password", dbPassword);

        return Persistence.createEntityManagerFactory("clinicPU", overrides);
    }

    private static String getEnvOr(Map<String, String> dotEnv, String key, String defaultValue) {
        String sysVal = System.getenv(key);
        if (sysVal != null && !sysVal.isBlank()) {
            return sysVal.trim();
        }
        if (dotEnv != null && dotEnv.containsKey(key)) {
            String val = dotEnv.get(key);
            if (val != null && !val.isBlank()) {
                return val.trim();
            }
        }
        return defaultValue;
    }

    private static Map<String, String> loadEnvMap() {
        Map<String, String> map = new HashMap<>();
        Path[] candidatePaths = new Path[] {
                Path.of(".env"),
                Path.of("../.env"),
                Path.of(System.getProperty("user.dir", "."), ".env")
        };
        for (Path p : candidatePaths) {
            if (Files.exists(p) && Files.isReadable(p)) {
                try (InputStream is = Files.newInputStream(p)) {
                    Properties props = new Properties();
                    props.load(is);
                    for (String name : props.stringPropertyNames()) {
                        map.put(name, props.getProperty(name));
                    }
                    break;
                } catch (Exception ignored) { }
            }
        }
        return map;
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