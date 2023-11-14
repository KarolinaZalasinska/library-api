package com.example.libraryapi.exceptions.reviews;

import com.example.libraryapi.model.Book;

import java.util.List;

public class DuplicateBookException extends RuntimeException{
    private List<Book> duplicates;

    public DuplicateBookException(String message, List<Book> duplicates) {
        super(message);
        this.duplicates = duplicates;
    }

    public List<Book> getDuplicates() {
        return duplicates;
    }
}
