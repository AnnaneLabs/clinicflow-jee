package com.clinicmanager.util;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.exception.AccountDisabledException;
import com.clinicmanager.exception.DuplicateEmailException;
import com.clinicmanager.exception.InvalidCredentialsException;
import com.clinicmanager.exception.UnauthorizedActionException;
import com.clinicmanager.exception.ValidationException;
import com.clinicmanager.model.User;
import com.clinicmanager.repository.UserRepository;
import com.clinicmanager.service.AuthService;

/** Temporary manual test of AuthService. Delete when real JUnit tests exist. */
public class AuthCheck {

    public static void main(String[] args) {
        AuthService auth = new AuthService();
        UserRepository users = new UserRepository();

        String email = "sara" + System.nanoTime() + "@clinic.test";

        // 1. register
        UserDTO registered = auth.register("Sara", "Alami", "  " + email.toUpperCase() + " ", "0600000000", "secret1");
        check("register returns a saved user", registered.getId() != null);
        check("email was normalized to lowercase", registered.getEmail().equals(email));

        User stored = users.findById(registered.getId()).orElseThrow();
        check("password is stored as a BCrypt hash", stored.getPasswordHash().startsWith("$2"));
        check("password is NOT stored in clear", !stored.getPasswordHash().contains("secret1"));

        // 2. rules on register
        expect("duplicate email (different case)", DuplicateEmailException.class,
                () -> auth.register("Other", "Person", email.toUpperCase(), null, "secret2"));
        expect("password shorter than 6", ValidationException.class,
                () -> auth.register("A", "B", "a" + System.nanoTime() + "@x.test", null, "12345"));
        expect("invalid email format", ValidationException.class,
                () -> auth.register("A", "B", "not-an-email", null, "secret1"));
        expect("blank first name", ValidationException.class,
                () -> auth.register("  ", "B", "b" + System.nanoTime() + "@x.test", null, "secret1"));

        // 3. login
        UserDTO loggedIn = auth.login(email, "secret1");
        check("login with correct credentials", loggedIn.getId().equals(registered.getId()));
        expect("login with wrong password", InvalidCredentialsException.class,
                () -> auth.login(email, "wrong-password"));
        expect("login with unknown email", InvalidCredentialsException.class,
                () -> auth.login("nobody@clinic.test", "secret1"));

        // 4. change password
        expect("change password with wrong current password", UnauthorizedActionException.class,
                () -> auth.changePassword(registered.getId(), "wrong", "newsecret"));
        expect("change password to a too-short one", ValidationException.class,
                () -> auth.changePassword(registered.getId(), "secret1", "abc"));
        auth.changePassword(registered.getId(), "secret1", "newsecret");
        expect("old password no longer works", InvalidCredentialsException.class,
                () -> auth.login(email, "secret1"));
        check("new password works", auth.login(email, "newsecret") != null);

        // 5. profile
        UserDTO updated = auth.updateProfile(registered.getId(), "Sarah", "Alami", "0611111111");
        check("profile updated", updated.getFirstName().equals("Sarah") && "0611111111".equals(updated.getPhone()));

        // 6. disabled account
        User toDisable = users.findById(registered.getId()).orElseThrow();
        toDisable.setActive(false);
        users.update(toDisable);
        expect("disabled account cannot log in", AccountDisabledException.class,
                () -> auth.login(email, "newsecret"));
        expect("disabled account + wrong password still looks like bad credentials",
                InvalidCredentialsException.class, () -> auth.login(email, "wrong"));

        JpaUtil.close();
    }

    private static void check(String label, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + label);
    }

    private static void expect(String label, Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
            System.out.println("FAIL  " + label + " (no exception thrown)");
        } catch (RuntimeException e) {
            if (type.isInstance(e)) {
                System.out.println("PASS  " + label + "  ->  " + e.getMessage());
            } else {
                System.out.println("FAIL  " + label + " (got " + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            }
        }
    }
}