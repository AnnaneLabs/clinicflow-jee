package com.clinicmanager.exception;

/**
 * Base class of every business-rule error in ClinicFlow.
 * Unchecked (RuntimeException): the transaction pattern in BaseRepository rolls back on it,
 * and services do not need "throws" clauses everywhere.
 * Servlets catch this one type to show the message to the user.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
