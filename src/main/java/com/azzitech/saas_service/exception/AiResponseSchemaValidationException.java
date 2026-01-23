package com.azzitech.saas_service.exception;

public class AiResponseSchemaValidationException extends RuntimeException {

    public AiResponseSchemaValidationException(String message) {
        super(message);
    }

    public AiResponseSchemaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
