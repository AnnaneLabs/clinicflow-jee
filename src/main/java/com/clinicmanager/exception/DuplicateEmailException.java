package com.clinicmanager.exception;

/** An account with this email already exists. */
public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException(String email) {
        super("An account with the email " + email + " already exists.");
    }
}
