package com.clinicmanager.exception;

/** The medical note is validated (read only) and cannot be modified. */
public class MedicalNoteLockedException extends BusinessException {

    public MedicalNoteLockedException() {
        super("This medical note is validated and can no longer be modified.");
    }
}
