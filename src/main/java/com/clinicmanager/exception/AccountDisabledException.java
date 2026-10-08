package com.clinicmanager.exception;

/** The account exists but has been disabled by an administrator. */
public class AccountDisabledException extends BusinessException {

    public AccountDisabledException() {
        super("This account is disabled. Please contact the clinic.");
    }
}
