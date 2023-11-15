package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;

import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final ModelMapper modelMapper;

    @Transactional
    public BookDto createBook(@Valid final BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        Book savedBook = repository.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }

    public BookDto getBookById(final Long id) {
        Optional<Book> optionalBook = repository.findById(id);
        return optionalBook.map(book -> modelMapper.map(book, BookDto.class))
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Book with the given ID was not found.", id));
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = repository.findAll();

        if (books.isEmpty()) {
            return Collections.emptyList();
        } else {
            return books.stream()
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public BookDto updateBook(final Long id, @Valid final BookDto bookDto) {
        return repository.findById(id)
                .map(book -> {
                    modelMapper.map(bookDto, book);
                    Book updatedBook = repository.save(book);
                    return modelMapper.map(updatedBook, BookDto.class);
                })
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The book with the given ID cannot be updated.", id));
    }

    @Transactional
    public void deleteBook(final Long id) {
        repository.deleteById(id);
    }
}
