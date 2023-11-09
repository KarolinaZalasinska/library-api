package service;

import dto.AuthorDto;
import exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.AuthorMapper;
import model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AuthorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    @Transactional
    public AuthorDto createAuthor(final AuthorDto authorDto) {
        Author author = mapper.toEntity(authorDto);
        Author savedAuthor = repository.save(author);
        return mapper.toDto(savedAuthor);
    }

    public AuthorDto getAuthorById(final Long id) {
        Optional<Author> optionalAuthor = repository.findById(id);
        return optionalAuthor.map(mapper::toDto).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Author with the given ID was not found.", id));
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = repository.findAll();

        if (authors.isEmpty()) {
            return Collections.emptyList();
        } else {
            return authors.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public AuthorDto updateAuthor(final Long id, AuthorDto authorDto) {
        return repository.findById(id)
                .map(author -> {
                    mapper.updateEntityFromDto(authorDto, author);
                    Author updateAuthor = repository.save(author);
                    return mapper.toDto(updateAuthor);
                })
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Author with the given ID was not found.", id));
    }

    @Transactional
    public void deleteAuthor(final Long id) {
        repository.deleteById(id);
    }
}
