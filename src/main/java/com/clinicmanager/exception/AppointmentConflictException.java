package com.clinicmanager.exception;

/** The doctor already has an appointment that overlaps this time range. */
public class AppointmentConflictException extends BusinessException {

    public AppointmentConflictException() {
        super("This time slot is already booked for this doctor.");
    }
}
