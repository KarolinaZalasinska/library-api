package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.LibraryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import com.example.libraryapi.exceptions.reviews.DuplicateBookException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.BookMapper;
import com.example.libraryapi.mapper.LibraryMapper;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Library;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.LibraryRepository;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {
    public final LibraryRepository repository;
    public final BookRepository bookRepository;
    public final LibraryMapper mapper;
    public final BookMapper bookMapper;

    @Transactional
    public LibraryDto createLibrary(final LibraryDto libraryDto) {
        Library library = mapper.toEntity(libraryDto);
        Library savedLibrary = repository.save(library);
        return mapper.toDto(savedLibrary);
    }

    public LibraryDto getLibraryById(final Long id) {
        Optional<Library> optionalLibrary = repository.findById(id);
        return optionalLibrary.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The library with the specified ID could not be found.", id));
    }

    public List<LibraryDto> getAllLibraries() {
        List<Library> libraries = repository.findAll();
        if (libraries.isEmpty()) {
            return Collections.emptyList();
        } else {
            return libraries.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public LibraryDto updateLibrary(final Long id, final LibraryDto libraryDto) {
        return repository.findById(id)
                .map(library -> {
                    mapper.updateEntityFromDto(libraryDto, library);
                    Library updateLibrary = repository.save(library);
                    return mapper.toDto(updateLibrary);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update library data.", id));
    }

    @Transactional
    public LibraryDto updateLibraryField(final Long id, final Map<String, String> fieldsToUpdate) {
        return repository.findById(id)
                .map(library -> {
                    for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
                        String field = entry.getKey();
                        String value = entry.getValue();

                        switch (field) {
                            case "name" -> library.setName(value);
                            case "address" -> library.setAddress(value);
                            case "postalCode" -> library.setPostalCode(value);
                            default -> throw new IllegalArgumentException("Invalid field specified: " + field);
                        }
                    }
                    Library updatedLibrary = repository.save(library);
                    return mapper.toDto(updatedLibrary);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update library data.", id));
    }

    @Transactional
    public void deleteLibrary(final Long id) {
        repository.deleteById(id);
    }

    public Set<BookDto> getBooksInLibrary(final Long id) {
        Optional<Library> optionalLibrary = repository.findById(id);

        return optionalLibrary.map(library -> {
            Set<Book> books = library.getBooks();
            return bookMapper.toDtoSet(books);
        }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Library not found", id));
    }

    @Transactional
    public LibraryDto addBookToLibrary(final Long libraryId, final Long bookId) {
        Library library = repository.findById(libraryId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Library not found with id: ", libraryId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Book not found with id: ", bookId));

        if (library.getBooks().contains(book)) {
            throw new DuplicateBookException("Book with id " + bookId + " is already exists in the library with id " + libraryId,
                    Collections.singletonList(book));
        }

        library.addBook(book);
        Library updatedLibrary = repository.save(library);

        return mapper.toDto(updatedLibrary);
    }

}