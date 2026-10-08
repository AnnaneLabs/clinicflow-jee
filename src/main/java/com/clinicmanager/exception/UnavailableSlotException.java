package com.clinicmanager.exception;

/** The requested time is outside the doctor availability, too close to now, or otherwise not bookable. */
public class UnavailableSlotException extends BusinessException {

    public UnavailableSlotException(String reason) {
        super(reason);
    }
}
