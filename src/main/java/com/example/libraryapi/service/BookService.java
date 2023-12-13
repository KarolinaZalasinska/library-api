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

/**
 * Service for managing books.
 */
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new book based on the provided BookDto.
     *
     * @param bookDto The BookDto containing information for the new book.
     * @return The created BookDto.
     */
    @Transactional
    public BookDto createBook(@Valid BookDto bookDto) {
        Book newBook = repository.save(modelMapper.map(bookDto, Book.class));
        return modelMapper.map(newBook, BookDto.class);
    }

    /**
     * Retrieves book information by bookId.
     *
     * @param id The identifier for the book.
     * @return The BookDto associated with the given bookId.
     * @throws ObjectNotFoundException if the book is not found.
     */
    public BookDto getBookById(final Long id) {
        return repository.findById(id)
                .map(book -> modelMapper.map(book, BookDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + id + " was not found."));
    }

    /**
     * Retrieves a list of all books.
     *
     * @return A List of BookDto representing all books.
     */
    public List<BookDto> getAllBooks() {
        return repository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing book based on the provided bookDto.
     *
     * @param id      The identifier for the book to be updated.
     * @param bookDto The BookDto containing updated information.
     * @return The updated BookDto.
     * @throws ObjectNotFoundException if the book is not found.
     */
    @Transactional
    public BookDto updateBook(final Long id, @Valid BookDto bookDto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + id + " was not found."));

        modelMapper.map(bookDto, book);
        Book updatedBook = repository.save(book);

        return modelMapper.map(updatedBook, BookDto.class);
    }

    /**
     * Deletes a book by its identifier.
     *
     * @param id The identifier for the book to be deleted.
     */
    @Transactional
    public void deleteBook(final Long id) {
        repository.deleteById(id);
    }
}
