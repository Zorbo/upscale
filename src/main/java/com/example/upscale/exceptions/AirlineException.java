package com.example.upscale.exceptions;

public class AirlineException extends RuntimeException {

    public AirlineException() {
    }

    public AirlineException(String message) {
        super(message);
    }

    public AirlineException(String message, Throwable cause) {
        super(message, cause);
    }
}
