package com.example.libraryapi.exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundInRepositoryException extends RuntimeException {
    private final Long id;

    public ObjectNotFoundInRepositoryException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
