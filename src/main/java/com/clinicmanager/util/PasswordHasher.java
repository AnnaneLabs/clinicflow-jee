package com.clinicmanager.util;

import org.mindrot.jbcrypt.BCrypt;

/** The one place that knows how passwords are hashed and checked. */
public final class PasswordHasher {

    private static final int COST = 10;

    /** Used to spend the same time on a check even when there is no real user. */
    private static final String DUMMY_HASH = BCrypt.hashpw("dummy-password", BCrypt.gensalt(COST));

    private PasswordHasher() { }

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    /**
     * True if the plain password matches the stored hash.
     * With a null hash (unknown user) it still runs one BCrypt check, then returns false,
     * so unknown emails and wrong passwords take the same time.
     */
    public static boolean matches(String plainPassword, String storedHash) {
        if (storedHash == null) {
            BCrypt.checkpw(plainPassword, DUMMY_HASH);
            return false;
        }
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}
