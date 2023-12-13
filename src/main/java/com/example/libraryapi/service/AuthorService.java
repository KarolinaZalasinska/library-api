package com.example.libraryapi.service;

import com.example.libraryapi.dto.AuthorDto;
import com.example.libraryapi.dto.LateFeeDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.model.LateFee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.model.Author;
import com.example.libraryapi.repository.AuthorRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing authors.
 */
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
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
//    public AuthorDto getAuthorById(final Long id) {
//        return repository.findById(id)
//                .map(author -> modelMapper.map(author, AuthorDto.class))
//                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + id + " was not found."));
//    }
    public AuthorDto getAuthorById(final Long id) {
        Author author = getAuthorByIdOrThrow(id);
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
     * Updates an existing author based on the provided authorDto.
     *
     * @param id        The identifier for the author to be updated.
     * @param authorDto The AuthorDto containing updated information.
     * @return The updated AuthorDto.
     * @throws ObjectNotFoundException if the author is not found.
     */
//    @Transactional
//    public AuthorDto updateAuthor(final Long id, @Valid AuthorDto authorDto) {
//        Author author = repository.findById(id)
//                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + id + " was not found."));
//
//        modelMapper.map(authorDto, author);
//        Author updatedAuthor = repository.save(author);
//
//        return modelMapper.map(updatedAuthor, AuthorDto.class);
//    }
    @Transactional
    public AuthorDto updateAuthor(final Long id, @Valid AuthorDto authorDto) {
        Author author = getAuthorByIdOrThrow(id);
        modelMapper.map(authorDto, author);
        Author updatedAuthor = repository.save(author);
        return modelMapper.map(updatedAuthor, AuthorDto.class);
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

    private Author getAuthorByIdOrThrow(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + id + " was not found."));
    }

}
