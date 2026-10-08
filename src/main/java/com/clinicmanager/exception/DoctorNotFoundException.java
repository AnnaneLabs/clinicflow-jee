package com.clinicmanager.exception;

/** No doctor matches the given id. */
public class DoctorNotFoundException extends BusinessException {

    public DoctorNotFoundException(Long id) {
        super("Doctor not found (id " + id + ").");
    }
}
