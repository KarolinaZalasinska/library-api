package com.example.libraryapi.service;

import com.example.libraryapi.dto.AuthorDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.model.Author;
import com.example.libraryapi.repository.AuthorRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
    private final ModelMapper modelMapper;

    @Transactional
    public AuthorDto createAuthor(@Valid AuthorDto authorDto) {
        Author author = repository.save(modelMapper.map(authorDto, Author.class));
        return modelMapper.map(author, AuthorDto.class);
    }

    public AuthorDto getAuthorById(final Long id) {
        return repository.findById(id)
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + id + " was not found."));
    }

    public List<AuthorDto> getAllAuthors() {
        return repository.findAll().stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthorDto updateAuthor(final Long id, @Valid AuthorDto authorDto) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Author with id " + id + " was not found."));

        modelMapper.map(authorDto, author);
        Author updatedAuthor = repository.save(author);

        return modelMapper.map(updatedAuthor, AuthorDto.class);
    }


    @Transactional
    public void deleteAuthor(final Long id) {
        repository.deleteById(id);
    }

}
