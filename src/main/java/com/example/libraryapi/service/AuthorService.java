package com.example.libraryapi.service;

import com.example.libraryapi.dto.AuthorDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;

import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.AuthorRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
    private final ModelMapper modelMapper;

    @Transactional
    public AuthorDto createAuthor(@Valid final AuthorDto authorDto) {
        Author author = modelMapper.map(authorDto, Author.class);
        Author savedAuthor = repository.save(author);
        return modelMapper.map(savedAuthor, AuthorDto.class);
    }

    public AuthorDto getAuthorById(final Long id) {
        Optional<Author> optionalAuthor = repository.findById(id);
        return optionalAuthor.map(author -> modelMapper.map(author, AuthorDto.class))
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Author with the given ID was not found.", id));
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = repository.findAll();

        if (authors.isEmpty()) {
            return Collections.emptyList();
        } else {
            return authors.stream()
                    .map(author -> modelMapper.map(author, AuthorDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public AuthorDto updateAuthor(final Long id, @Valid final AuthorDto authorDto) {
        return repository.findById(id)
                .map(author -> {
                    modelMapper.map(authorDto, author);
                    Author updateAuthor = repository.save(author);
                    return modelMapper.map(updateAuthor, AuthorDto.class);
                })
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Author with the given ID was not found.", id));
    }

    @Transactional
    public void deleteAuthor(final Long id) {
        repository.deleteById(id);
    }
}
