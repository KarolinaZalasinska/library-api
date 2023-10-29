package service;

import dto.AuthorDto;
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

    //    @Transactional
//    public AuthorDto createAuthor(AuthorDto authorDto) {
//        Author author = authorMapper.authorDtoToAuthor(authorDto);
//        Author savedAuthor = authorRepository.save(author);
//        return authorMapper.authorToAuthorDto(savedAuthor);
//    }
    @Transactional
    public AuthorDto createAuthor(final AuthorDto authorDto) {
        Author author = mapper.toEntity(authorDto);
        Author savedAuthor = repository.save(author);
        return mapper.toDto(savedAuthor);
    }

    //    @Transactional - reczna aktualizacja
//    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
//        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
//
//        if (optionalAuthor.isPresent()) {
//            Author author = optionalAuthor.get();
//            // Zaktualizuj dane autora na podstawie AuthorDto
//            author.setFirstName(authorDto.getFirstName());
//            author.setLastName(authorDto.getLastName());
//            // Możesz także obsługiwać inne pola, jeśli są dostępne w AuthorDto
//            Author updatedAuthor = authorRepository.save(author);
//            return authorMapper.toDto(updatedAuthor);
//        } else {
//            // Handle author not found
//            return null;
//        }
//    }

//    @Transactional
//    public AuthorDto updateAuthor(final Long id, final AuthorDto authorDto) {
//        Optional<Author> optionalAuthor = authorRepository.findById(id);
//
//        if (optionalAuthor.isPresent()) {
//            Author author = optionalAuthor.get();
//            // Przypisz wartości z AuthorDto do istniejącego autora
//            authorMapper.toEntity(authorDto, author);
//            Author updateAuthor = authorRepository.save(author);
//            return authorMapper.toDto(updateAuthor);
//        } else {
//            throw new AuthorNotFoundException(id);
//        }
//    }

    public AuthorDto getAuthorById(final Long id) {
        Optional<Author> optionalAuthor = repository.findById(id);
        return optionalAuthor.map(mapper::toDto).orElseThrow(() -> new AuthorNotFoundException(id));
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
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }


    @Transactional
    public void deleteAuthor(final Long id) {
        repository.deleteById(id);
    }
}
