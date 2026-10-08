package com.clinicmanager.exception;

/** Input data breaks a rule (for example a password shorter than 6 characters). */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }
}
