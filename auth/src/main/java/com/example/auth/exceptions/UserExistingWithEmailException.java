package com.example.auth.exceptions;

public class UserExistingWithEmailException extends RuntimeException {
    public UserExistingWithEmailException(String message) {
        super(message);
    }

    public UserExistingWithEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistingWithEmailException(Throwable cause) {
        super(cause);
    }
}

