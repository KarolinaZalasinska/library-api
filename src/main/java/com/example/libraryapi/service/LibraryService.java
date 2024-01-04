package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.LibraryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.exceptions.reviews.DuplicateBookException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Library;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.LibraryRepository;

import javax.validation.Valid;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing libraries.
 */
@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new library based on the provided LibraryDto.
     *
     * @param libraryDto The LibraryDto containing information for the new library.
     * @return The created LibraryDto.
     */
    @Transactional
    public LibraryDto createLibrary(@Valid LibraryDto libraryDto) {
        Library newLibrary = libraryRepository.save(modelMapper.map(libraryDto, Library.class));
        return modelMapper.map(newLibrary, LibraryDto.class);
    }

    /**
     * Retrieves a library by its identifier.
     *
     * @param id The identifier of the library to be retrieved.
     * @return The LibraryDto associated with the given id.
     * @throws ObjectNotFoundException if the library is not found.
     */
    public LibraryDto getLibraryById(final Long id) {
        Library library = getLibraryOrThrow(id);
        return modelMapper.map(library, LibraryDto.class);
    }

    /**
     * Retrieves all libraries.
     *
     * @return A list of LibraryDto representing all libraries.
     */
    public List<LibraryDto> getAllLibraries() {
        return libraryRepository.findAll().stream()
                .map(library -> modelMapper.map(library, LibraryDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing library based on the provided fields.
     *
     * @param id             The identifier for the library to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @return The updated Library.
     * @throws ObjectNotFoundException  if the library is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public Library updateLibrary(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Library library = getLibraryOrThrow(id);

        Map<String, BiConsumer<Library, String>> fieldSetters = Map.of(
                "name", Library::setName,
                "address", Library::setAddress,
                "postalCode", Library::setPostalCode
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            BiConsumer<Library, String> fieldSetter = fieldSetters.get(field);
            if (fieldSetter != null) {
                fieldSetter.accept(library, value);
            } else {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }
        }

        validateLibraryFields(library);

        return libraryRepository.save(library);
    }

    private void validateLibraryFields(Library library) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Library>> violations = validator.validate(library);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Library validation failed: " + violations);
        }
    }

    /**
     * Deletes a library by its identifier.
     *
     * @param id The identifier of the library to be deleted.
     */
    @Transactional
    public void deleteLibrary(final Long id) {
        libraryRepository.deleteById(id);
    }

    /**
     * Retrieves the set of books in the library with the specified ID.
     *
     * @param id The identifier for the library.
     * @return A Set of BookDto representing the books in the library.
     * @throws ObjectNotFoundException If the library with the given ID is not found.
     */
    public Set<BookDto> getBooksInLibrary(final Long id) {
        Library library = getLibraryOrThrow(id);

        return library.getBooks().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toSet());
    }

    /**
     * Adds a book with the specified ID to the library with the given ID.
     *
     * @param libraryId The identifier for the library.
     * @param bookId    The identifier for the book.
     * @return The LibraryDto representing the updated library.
     * @throws ObjectNotFoundException If the library or book with the given IDs is not found.
     * @throws DuplicateBookException  If the book is already present in the library.
     */
    @Transactional
    public LibraryDto addBookToLibrary(final Long libraryId, final Long bookId) {
        Library library = getLibraryOrThrow(libraryId);

        Book book = bookService.getBookOrThrow(bookId);

        if (library.getBooks().contains(book)) {
            throw new DuplicateBookException("Book with id " + bookId + " already exists in the library with id " + libraryId,
                    Collections.singletonList(book));
        }

        library.addBook(book);
        Library updatedLibrary = libraryRepository.save(library);

        return modelMapper.map(updatedLibrary, LibraryDto.class);
    }

    Library getLibraryOrThrow(final Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ObjectNotFoundException("Library with id " + libraryId + " was not found."));
    }
}