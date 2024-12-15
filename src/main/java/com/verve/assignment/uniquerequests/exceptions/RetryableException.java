package com.verve.assignment.uniquerequests.exceptions;

public class RetryableException extends RuntimeException {
    public RetryableException(String message) {
        super(message);
    }
}
