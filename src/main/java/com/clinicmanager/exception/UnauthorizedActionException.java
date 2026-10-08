package com.clinicmanager.exception;

/** The user is not allowed to perform this action (wrong owner, wrong role, or cancellation window passed). */
public class UnauthorizedActionException extends BusinessException {

    public UnauthorizedActionException() {
        super("You are not allowed to perform this action.");
    }

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
