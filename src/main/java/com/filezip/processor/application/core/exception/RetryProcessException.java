package com.filezip.processor.application.core.exception;

public class RetryProcessException extends RuntimeException {
    public RetryProcessException() {
    }

    public RetryProcessException(String message) {
        super(message);
    }
}
