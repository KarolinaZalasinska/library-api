package com.example.libraryapi.exceptions.reviews;

public class InvalidRatingException extends RuntimeException{
    public InvalidRatingException(String message) {
        super(message);
    }
}
