package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.PublisherDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.BookMapper;
import com.example.libraryapi.mapper.PublisherMapper;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.PublisherRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository repository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PublisherMapper mapper;

    @Transactional
    public PublisherDto createPublisher(final PublisherDto publisherDto) {
        Publisher publisher = mapper.toEntity(publisherDto);
        Publisher savedPublisher = repository.save(publisher);
        return mapper.toDto(savedPublisher);
    }

    public PublisherDto getPublisherById(final Long id) {
        Optional<Publisher> optionalPublisher = repository.findById(id);
        return optionalPublisher.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Publication with the given id was not found.", id));
    }

    public List<PublisherDto> getAllPublishers() {
        List<Publisher> publishers = repository.findAll();

        if (publishers.isEmpty()) {
            return Collections.emptyList();
        } else {
            return publishers.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public PublisherDto updatePublisher(final Long id, final PublisherDto publisherDto) {
        return repository.findById(id)
                .map(publisher -> {
                    mapper.updateEntityFromDto(publisherDto, publisher);
                    Publisher updatePublisher = repository.save(publisher);
                    return mapper.toDto(updatePublisher);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Publication with the given id was not found.", id));
    }

    @Transactional
    public void deletePublisher(final Long id) {
        repository.deleteById(id);
    }

    public List<BookDto> getBooksByPublisher(final Long publisherId) {
        Publisher publisher = repository.findById(publisherId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Publisher with the given ID was not found.", publisherId));

        List<Book> books = bookRepository.findByPublisher(publisher);
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
