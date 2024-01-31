package com.example.auth.exceptions;

public class UserExistingWithNameException extends RuntimeException{
    public UserExistingWithNameException(String message) {
        super(message);
    }

    public UserExistingWithNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistingWithNameException(Throwable cause) {
        super(cause);
    }
}

