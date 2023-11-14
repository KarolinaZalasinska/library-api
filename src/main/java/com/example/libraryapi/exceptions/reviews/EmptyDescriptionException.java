package com.example.libraryapi.exceptions.reviews;

public class EmptyDescriptionException extends RuntimeException {
    public EmptyDescriptionException(String message) {
        super(message);
    }
}
