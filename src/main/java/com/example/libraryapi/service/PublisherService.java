package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.PublisherDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.PublisherRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PublisherDto createPublisher(@Valid final PublisherDto publisherDto) {
        Publisher publisher = modelMapper.map(publisherDto, Publisher.class);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return modelMapper.map(savedPublisher, PublisherDto.class);
    }

    public PublisherDto getPublisherById(final Long id) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(id);
        return optionalPublisher.map(publisher -> modelMapper.map(publisher, PublisherDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Publication with id " + id + " was not found."));
    }

    public List<PublisherDto> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();

        if (publishers.isEmpty()) {
            return Collections.emptyList();
        } else {
            return publishers.stream()
                    .map(publisher -> modelMapper.map(publisher, PublisherDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public PublisherDto updatePublisher(final Long id, final PublisherDto publisherDto) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    modelMapper.map(publisherDto, publisher);
                    Publisher updatePublisher = publisherRepository.save(publisher);
                    return modelMapper.map(updatePublisher, PublisherDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("Publication with id " + id + " was not found."));
    }

    @Transactional
    public void deletePublisher(final Long id) {
        publisherRepository.deleteById(id);
    }

    public List<BookDto> getBooksByPublisher(final Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Publication with id " + id + " was not found."));

        List<Book> books = bookRepository.findByPublisher(publisher);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }
}
