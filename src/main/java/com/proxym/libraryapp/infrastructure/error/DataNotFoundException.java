package com.proxym.libraryapp.infrastructure.error;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
