package com.clinicmanager.exception;

/** A doctor with this matricule already exists. */
public class DuplicateMatriculeException extends BusinessException {

    public DuplicateMatriculeException(String matricule) {
        super("A doctor with the matricule " + matricule + " already exists.");
    }
}
