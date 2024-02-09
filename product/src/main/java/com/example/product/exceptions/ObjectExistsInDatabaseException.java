package com.example.product.exceptions;

public class ObjectExistsInDatabaseException extends RuntimeException {
    public ObjectExistsInDatabaseException(String message) {
        super(message);
    }

    public ObjectExistsInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectExistsInDatabaseException(Throwable cause) {
        super(cause);
    }

}
