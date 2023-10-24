package service;

import dto.AuthorDto;
import exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import mapper.AuthorMapper;
import model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    //    @Transactional
//    public AuthorDto createAuthor(AuthorDto authorDto) {
//        Author author = authorMapper.authorDtoToAuthor(authorDto);
//        Author savedAuthor = authorRepository.save(author);
//        return authorMapper.authorToAuthorDto(savedAuthor);
//    }
    @Transactional
    public AuthorDto createAuthor(final AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
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
    @Transactional
    public AuthorDto updateAuthor(final Long id, final AuthorDto authorDto) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            // Przypisz wartości z AuthorDto do istniejącego autora
            authorMapper.toEntity(authorDto, author);
            Author updatedAuthor = authorRepository.save(author);
            return authorMapper.toDto(updatedAuthor);
        } else {
            // Obsłuż sytuację, gdy autor nie zostanie znaleziony
            throw new AuthorNotFoundException(id);
        }
    }


    @Transactional
    public void deleteAuthor(final Long id) {
        authorRepository.deleteById(id);
    }

    public AuthorDto getAuthorById(final Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        return optionalAuthor.map(authorMapper::toDto).orElse(null);
    }

    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            return null;
        }

        return authors.stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }
}
