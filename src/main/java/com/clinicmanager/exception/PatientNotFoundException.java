package com.clinicmanager.exception;

/** No patient profile matches the given id or account. */
public class PatientNotFoundException extends BusinessException {

    public PatientNotFoundException(Long id) {
        super("Patient not found (id " + id + ").");
    }

    private PatientNotFoundException(String message) {
        super(message);
    }

    public static PatientNotFoundException forUser(Long userId) {
        return new PatientNotFoundException("No patient profile for this account (user " + userId + ").");
    }
}
