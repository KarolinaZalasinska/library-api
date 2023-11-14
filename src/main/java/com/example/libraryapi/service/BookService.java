package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.BookMapper;
import com.example.libraryapi.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final BookMapper mapper;

    @Transactional
    public BookDto createBook(@Valid final BookDto bookDto) {
        Book book = mapper.toEntity(bookDto);
        Book savedBook = repository.save(book);
        return mapper.toDto(savedBook);
    }

    public BookDto getBookById(final Long id) {
        Optional<Book> optionalBook = repository.findById(id);
        return optionalBook.map(mapper::toDto).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Book with the given ID was not found.", id));
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = repository.findAll();

        if (books.isEmpty()) {
            return Collections.emptyList();
        } else {
            return books.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

//    @Transactional
//    public BookDto updateBook(final Long id, final BookDto bookDto) {
//        Optional<Book> optionalBook = bookRepository.findById(id);
//        if (optionalBook.isPresent()) {
//            Book book = optionalBook.get();
//            bookMapper.toEntity(bookDto, book);
//            Book updateBook = bookRepository.save(book);
//            return bookMapper.toDto(updateBook);
//        }).orElseThrow(() -> new BookNotFoundException(id));
//    }

    @Transactional
    public BookDto updateBook(final Long id, @Valid final BookDto bookDto) {
        return repository.findById(id)
                .map(book -> {
                    mapper.updateEntityFromDto(bookDto, book);
                    Book updateBook = repository.save(book);
                    return mapper.toDto(updateBook);
                })
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The book with the given ID cannot be updated.", id));
    }

    @Transactional
    public void deleteBook(final Long id) {
        repository.deleteById(id);
    }
}
