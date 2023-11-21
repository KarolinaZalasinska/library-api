package com.example.libraryapi.exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
