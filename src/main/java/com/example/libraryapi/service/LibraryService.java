package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.LibraryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.exceptions.reviews.DuplicateBookException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Library;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.LibraryRepository;

import javax.validation.Valid;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public LibraryDto createLibrary(@Valid LibraryDto libraryDto) {
        Library newLibrary = libraryRepository.save(modelMapper.map(libraryDto, Library.class));
        return modelMapper.map(newLibrary, LibraryDto.class);
    }

    public LibraryDto getLibraryById(final Long id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        return optionalLibrary.map(library -> modelMapper.map(library, LibraryDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Library with id " + id + " was not found."));
    }

    public List<LibraryDto> getAllLibraries() {
        List<Library> libraries = libraryRepository.findAll();
        if (libraries.isEmpty()) {
            return Collections.emptyList();
        } else {
            return libraries.stream()
                    .map(library -> modelMapper.map(library, LibraryDto.class))
                    .collect(Collectors.toList());
        }
    }

    public Library updateLibrary(Long id, Map<String, String> fieldsToUpdate) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Library with id " + id + " was not found."));

        Map<String, BiConsumer<Library, String>> fieldSetters = Map.of(
                "name", Library::setName,
                "address", Library::setAddress,
                "postalCode", Library::setPostalCode
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (lib, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(library, value);
        }

        return libraryRepository.save(library);
    }


    @Transactional
    public void deleteLibrary(final Long id) {
        libraryRepository.deleteById(id);
    }

    public Set<BookDto> getBooksInLibrary(final Long id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);

        return optionalLibrary.map(library -> {
            Set<Book> books = library.getBooks();
            return books.stream()
                    .map(book -> modelMapper.map(book, BookDto.class))
                    .collect(Collectors.toSet());
        }).orElseThrow(() -> new ObjectNotFoundException("Library with ID " + id + " was not found."));
    }

    @Transactional
    public LibraryDto addBookToLibrary(final Long libraryId, final Long bookId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ObjectNotFoundException("Library with id " + libraryId + " was not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book with id " + bookId + " was not found."));

        if (library.getBooks().contains(book)) {
            throw new DuplicateBookException("Book with id " + bookId + " already exists in the library with id " + libraryId,
                    Collections.singletonList(book));
        }

        library.addBook(book);
        Library updatedLibrary = libraryRepository.save(library);

        return modelMapper.map(updatedLibrary, LibraryDto.class);
    }

}