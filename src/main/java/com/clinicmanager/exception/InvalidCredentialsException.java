package com.clinicmanager.exception;

/** Login failed. The message is deliberately the same for an unknown email and a wrong password. */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super("Invalid email or password.");
    }
}
