package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final ModelMapper modelMapper;

    @Transactional
    public BookDto createBook(@Valid BookDto bookDto) {
        Book book = repository.save(modelMapper.map(bookDto, Book.class));
        return modelMapper.map(book, BookDto.class);
    }

    public BookDto getBookById(final Long id) {
        return repository.findById(id)
                .map(book -> modelMapper.map(book, BookDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + id + " was not found."));
    }

    public List<BookDto> getAllBooks() {
        return repository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDto updateBook(final Long id, @Valid BookDto bookDto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + id + " was not found."));

        modelMapper.map(bookDto, book);
        Book updatedBook = repository.save(book);

        return modelMapper.map(updatedBook, BookDto.class);
    }


    @Transactional
    public void deleteBook(final Long id) {
        repository.deleteById(id);
    }
}
