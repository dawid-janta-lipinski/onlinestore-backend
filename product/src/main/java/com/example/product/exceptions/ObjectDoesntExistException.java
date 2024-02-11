package com.example.product.exceptions;

public class ObjectDoesntExistException extends RuntimeException {
    public ObjectDoesntExistException(String message) {
        super(message);
    }

    public ObjectDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectDoesntExistException(Throwable cause) {
        super(cause);
    }
}
