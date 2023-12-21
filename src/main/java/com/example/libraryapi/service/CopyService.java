package com.example.libraryapi.service;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.model.Copy;
import com.example.libraryapi.repository.CopyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing copies.
 */
@Service
@RequiredArgsConstructor
public class CopyService {
    private final CopyRepository copyRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new copy based on the provided CopyDto.
     *
     * @param copyDto The CopyDto containing information for the new copy.
     * @return The created CopyDto.
     */
    @Transactional
    public CopyDto createCopy(@Valid CopyDto copyDto) {
        Copy newCopy = copyRepository.save(modelMapper.map(copyDto, Copy.class));
        return modelMapper.map(newCopy, CopyDto.class);
    }

    /**
     * Retrieves a copy by its identifier.
     *
     * @param id The identifier of the copy to be retrieved.
     * @return The CopyDto associated with the given id.
     * @throws ObjectNotFoundException if the copy is not found.
     */
    public CopyDto getCopyById(final Long id) {
        Copy copy = getCopyOrThrow(id);
        return modelMapper.map(copy, CopyDto.class);
    }

    /**
     * Retrieves all copies.
     *
     * @return A list of CopyDto representing all copies.
     */
    public List<CopyDto> getAllCopies() {
        return copyRepository.findAll().stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates the fields of a Copy object based on the provided data.
     *
     * @param id             The identifier of the instance to be updated.
     * @param fieldsToUpdate A map containing fields to be updated in the form of (field name, value) pairs.
     * @throws IllegalArgumentException If fieldsToUpdate is null or empty.
     * @throws ObjectNotFoundException  if the copy is not found.
     */
    public void updateCopy(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Copy copy = getCopyOrThrow(id);

        Map<String, BiConsumer<Copy, String>> fieldSetters = Map.of(
                "purchaseDate", (c, val) -> {
                    LocalDate purchaseDate = LocalDate.parse(val);
                    validatePurchaseDate(purchaseDate);
                    c.setPurchaseDate(purchaseDate);
                },
                "returnDate", (c, val) -> {
                    LocalDate returnDate = LocalDate.parse(val);
                    validateReturnDate(returnDate);
                    c.setReturnDate(returnDate);
                }
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (cl, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(copy, value);
        }
    }

    private void validatePurchaseDate(LocalDate purchaseDate) {
        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date must be in the past or present.");
        }
    }

    private void validateReturnDate(LocalDate returnDate) {
        if (returnDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Return date must be in the past or present.");
        }
    }

    /**
     * Delete a copy by its identifier.
     *
     * @param id The identifier of the copy to be deleted.
     */
    @Transactional
    public void deleteCopy(final Long id) {
        copyRepository.deleteById(id);
    }

    /**
     * Get copies for a specific book.
     *
     * @param bookId The identifier of the book.
     * @return A list of CopyDto representing copies for the book.
     */
    public List<CopyDto> getCopiesForBook(final Long bookId) {
        return copyRepository.findByBookId(bookId).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get all currently available copies.
     *
     * @return A set of CopyDto representing available copies.
     */
    public Set<CopyDto> getAvailableCopiesNow() {
        return copyRepository.findAvailableCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toSet());
    }

    /**
     * Get available copies for a specific book.
     *
     * @param bookId The identifier of the book.
     * @return A list of CopyDto representing available copies for the book.
     */
    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        return copyRepository.findAvailableCopiesForBook(bookId, LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get borrowed copies for a specific client.
     *
     * @param clientId The identifier of the client.
     * @return A list of CopyDto representing borrowed copies for the client.
     */
    public List<CopyDto> getBorrowedCopiesForClient(final Long clientId) {
        return copyRepository.findBorrowedCopiesByClientId(clientId).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get overdue copies.
     *
     * @return A list of CopyDto representing overdue copies.
     */
    public List<CopyDto> getOverdueCopies() {
        return copyRepository.findOverdueCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get details for a specific copy.
     *
     * @param id The identifier of the copy.
     * @return The CopyDto representing details for the copy.
     * @throws ObjectNotFoundException if the copy is not found.
     */
    public CopyDto getCopyDetails(final Long id) {
        Copy copy = getCopyOrThrow(id);
        return modelMapper.map(copy, CopyDto.class);
    }

    /**
     * Get currently borrowed copies.
     *
     * @return A list of CopyDto representing currently borrowed copies.
     */
    public List<CopyDto> getCurrentlyBorrowedCopies() {
        return copyRepository.findCurrentlyBorrowedCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get a copy by its identifier or throw an exception if not found.
     *
     * @param copyId The identifier of the copy.
     * @return The Copy entity.
     * @throws ObjectNotFoundException if the copy is not found.
     */
    Copy getCopyOrThrow(final Long copyId) {
        return copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + copyId + " was not found."));
    }
}

