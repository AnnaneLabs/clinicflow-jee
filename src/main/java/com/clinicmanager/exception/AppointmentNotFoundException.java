package com.clinicmanager.exception;

/** No appointment matches the given id. */
public class AppointmentNotFoundException extends BusinessException {

    public AppointmentNotFoundException(Long id) {
        super("Appointment not found (id " + id + ").");
    }
}
