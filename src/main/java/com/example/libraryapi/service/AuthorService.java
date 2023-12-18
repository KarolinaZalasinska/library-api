package com.example.libraryapi.service;

import com.example.libraryapi.dto.AuthorDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.model.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.model.Author;
import com.example.libraryapi.repository.AuthorRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing authors.
 */
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new author based on the provided AuthorDto.
     *
     * @param authorDto The AuthorDto containing information for the new author.
     * @return The created AuthorDto.
     */
    @Transactional
    public AuthorDto createAuthor(@Valid AuthorDto authorDto) {
        Author newAuthor = repository.save(modelMapper.map(authorDto, Author.class));
        return modelMapper.map(newAuthor, AuthorDto.class);
    }

    /**
     * Retrieves author information by authorId.
     *
     * @param id The identifier for the author.
     * @return The AuthorDto associated with the given authorId.
     * @throws ObjectNotFoundException if the author is not found.
    */
    public AuthorDto getAuthorById(final Long id) {
        Author author = getAuthorOrThrow(id);
        return modelMapper.map(author, AuthorDto.class);
    }

    /**
     * Retrieves a list of all authors.
     *
     * @return A List of AuthorDto representing all authors.
     */
    public List<AuthorDto> getAllAuthors() {
        return repository.findAll().stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }


    /**
     * Updates an existing author based on the provided fields.
     *
     * @param id             The identifier for the author to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @throws ObjectNotFoundException  if the author is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public void updateAuthor(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Author author = getAuthorOrThrow(id);

        Map<String, BiConsumer<Author, String>> fieldSetters = Map.of(
                "firstName", (auth, val) -> {
                    if (isValidName(val)) {
                        auth.setFirstName(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for firstName: " + val);
                    }
                },
                "lastName", (auth, val) -> {
                    if (isValidName(val)) {
                        auth.setLastName(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for lastName: " + val);
                    }
                }
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (auth, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(author, value);
        }
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Deletes an author by its identifier.
     *
     * @param id The identifier for the author to be deleted.
     */
    @Transactional
    public void deleteAuthor(final Long id) {
        repository.deleteById(id);
    }

    /**
     * Adds a book to the specified author.
     *
     * @param authorId The identifier of the author.
     * @param bookId   The identifier of the book to be added.
     * @return The updated AuthorDto.
     * @throws ObjectNotFoundException  if the author or book is not found.
     * @throws IllegalArgumentException if the book is already associated with the author.
     */
    @Transactional
    public AuthorDto addBookToAuthor(Long authorId, Long bookId) {
        Author author = getAuthorOrThrow(authorId);
        Book book = bookService.getBookOrThrow(bookId);

        if (author.getBooks().contains(book)) {
            throw new IllegalArgumentException("Book is already associated with the author.");
        }

        author.getBooks().add(book);
        repository.save(author);

        return modelMapper.map(author, AuthorDto.class);
    }

    Author getAuthorOrThrow(final Long authorId) {
        return repository.findById(authorId)
                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + authorId + " was not found."));
    }

}
