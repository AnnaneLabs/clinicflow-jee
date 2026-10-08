package com.clinicmanager.exception;

/** No user matches the given id or email. */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id) {
        super("User not found (id " + id + ").");
    }

    public UserNotFoundException(String email) {
        super("No user found with email " + email + ".");
    }
}
