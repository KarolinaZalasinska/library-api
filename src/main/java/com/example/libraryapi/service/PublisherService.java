package com.example.libraryapi.service;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.PublisherDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.PublisherRepository;

import javax.validation.Valid;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing publishers.
 */
@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new publisher based on the provided PublisherDto.
     *
     * @param publisherDto The PublisherDto containing information for the new publisher.
     * @return The created PublisherDto.
     */
    @Transactional
    public PublisherDto createPublisher(@Valid PublisherDto publisherDto) {
        Publisher newPublisher = publisherRepository.save(modelMapper.map(publisherDto, Publisher.class));
        return modelMapper.map(newPublisher, PublisherDto.class);
    }

    /**
     * Retrieves a publisher by its identifier.
     *
     * @param id The identifier of the publisher to be retrieved.
     * @return The PublisherDto associated with the given id.
     * @throws ObjectNotFoundException if the publisher is not found.
     */
    public PublisherDto getPublisherById(final Long id) {
        Publisher publisher = getPublisherOrThrow(id);
        return modelMapper.map(publisher, PublisherDto.class);
    }

    /**
     * Retrieves all publishers.
     *
     * @return A list of PublisherDto representing all publishers.
     */
    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisher -> modelMapper.map(publisher, PublisherDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing publisher based on the provided fields.
     *
     * @param id             The identifier for the publisher to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @return The updated PublisherDto.
     * @throws ObjectNotFoundException  if the publisher with the given ID is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public PublisherDto updatePublisher(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Publisher publisher = getPublisherOrThrow(id);

        Map<String, BiConsumer<Publisher, String>> fieldSetters = Map.of(
                "name", Publisher::setName,
                "address", Publisher::setAddress,
                "postalCode", Publisher::setPostalCode
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            BiConsumer<Publisher, String> fieldSetter = fieldSetters.get(field);
            if (fieldSetter != null) {
                fieldSetter.accept(publisher, value);
            } else {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }
        }

        validatePublisherFields(publisher);

        Publisher updatedPublisher = publisherRepository.save(publisher);

        return modelMapper.map(updatedPublisher, PublisherDto.class);
    }

    private void validatePublisherFields(Publisher publisher) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Publisher validation failed: " + violations);
        }
    }

    /**
     * Deletes a publisher by its identifier.
     *
     * @param id The identifier of the publisher to be deleted.
     */
    @Transactional
    public void deletePublisher(final Long id) {
        publisherRepository.deleteById(id);
    }

    /**
     * Retrieves a list of books associated with the publisher identified by the given ID.
     *
     * @param id The identifier of the publisher.
     * @return A List of BookDto representing the books associated with the publisher.
     * @throws ObjectNotFoundException If the publisher with the given ID is not found.
     */
    public List<BookDto> getBooksByPublisher(final Long id) {
        Publisher publisher = getPublisherOrThrow(id);

        List<Book> books = bookRepository.findByPublisher(publisher);

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    Publisher getPublisherOrThrow(final Long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ObjectNotFoundException("Publisher with id " + publisherId + " was not found."));
    }
}
