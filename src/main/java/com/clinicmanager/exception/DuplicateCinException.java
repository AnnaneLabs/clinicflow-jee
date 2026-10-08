package com.clinicmanager.exception;

/** A patient with this CIN (national id) already exists. */
public class DuplicateCinException extends BusinessException {

    public DuplicateCinException(String cin) {
        super("A patient with the CIN " + cin + " already exists.");
    }
}
