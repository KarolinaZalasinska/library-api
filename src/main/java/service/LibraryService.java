package service;

import dto.BookDto;
import dto.LibraryDto;
import exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.BookMapper;
import mapper.LibraryMapper;
import model.Book;
import model.Library;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BookRepository;
import repository.LibraryRepository;

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

    @Transactional
    public Set<BookDto> getBooksInLibrary(final Long id) {
        Optional<Library> optionalLibrary = repository.findById(id);

        return optionalLibrary.map(library -> {
            Set<Book> books = library.getBooks();
            return bookMapper.toDtoSet(books);
        }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Library not found", id));
    }

    @Transactional
    public Optional<LibraryDto> addBookToLibrary(Long libraryId, Long bookId) {
        return repository.findById(libraryId).flatMap(library -> bookRepository.findById(bookId)
                .map(book -> {
                    library.addBook(book);
                    Library updatedLibrary = repository.save(library);
                    return Optional.of(mapper.toDto(updatedLibrary));
                })
                .orElseGet(Optional::empty));
    }

}