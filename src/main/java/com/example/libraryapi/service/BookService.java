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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing books.
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
        Book book = getBookOrThrow(id);
        return modelMapper.map(book, BookDto.class);
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
     * Updates an existing book based on the provided fields.
     *
     * @param id             The identifier for the book to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @throws ObjectNotFoundException  if the book is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public void updateBook(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Book book = getBookOrThrow(id);

        Map<String, BiConsumer<Book, String>> fieldSetters = Map.of(
                "title", (b, val) -> {
                    if (isValidTitle(val)) {
                        b.setTitle(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for title: " + val);
                    }
                },
                "isbn", (b, val) -> {
                    if (isValidIsbn(val)) {
                        b.setIsbn(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for ISBN: " + val);
                    }
                }
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (b, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(book, value);
        }
    }

    private boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    private boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return isbn.matches("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$");
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

    Book getBookOrThrow(final Long bookId) {
        return repository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + bookId + " was not found."));
    }

}
