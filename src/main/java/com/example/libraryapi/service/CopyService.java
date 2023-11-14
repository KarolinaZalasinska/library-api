package com.example.libraryapi.service;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import com.example.libraryapi.model.Copy;
import com.example.libraryapi.repository.CopyRepository;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.CopyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CopyService {
    private final CopyRepository copyRepository;
    private final CopyMapper copyMapper;

    @Transactional
    public CopyDto createCopy(final CopyDto copyDto) {
        Copy copy = copyMapper.toEntity(copyDto);
        Copy savedCopy = copyRepository.save(copy);
        return copyMapper.toDto(savedCopy);
    }

    public CopyDto getCopyById(final Long id) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);
        return optionalCopy.map(copyMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The copy with the given ID could not be found.", id));
    }

    public List<CopyDto> getAllCopies() {
        List<Copy> copies = copyRepository.findAll();
        if (copies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return copies.stream()
                    .map(copyMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CopyDto updateCopy(final Long id, CopyDto copyDto) {
        return copyRepository.findById(id)
                .map(copy -> {
                    copyMapper.updateEntityFromDto(copyDto, copy);
                    Copy updatedCopy = copyRepository.save(copy);
                    return copyMapper.toDto(updatedCopy);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update the copy with the given ID.", id));
    }

    @Transactional
    public void deleteCopy(final Long id) {
        copyRepository.deleteById(id);
    }

    public List<CopyDto> getCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findByBookId(bookId);
        return copies.stream()
                .map(copyMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<CopyDto> getAvailableCopiesNow() {
        List<Copy> availableCopies = copyRepository.findAvailableCopies(LocalDate.now());
        return availableCopies.stream()
                .map(copyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findAvailableCopiesForBook(bookId, LocalDate.now());
        if (copies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return copies.stream()
                    .map(copyMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    public List<CopyDto> getBorrowedCopiesForUser(final Long userId) {
        List<Copy> copies = copyRepository.findBorrowedCopiesForUser(userId);
        return copies.stream()
                .map(copyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getOverdueCopies() {
        List<Copy> copies = copyRepository.findOverdueCopies(LocalDate.now());
        return copies.stream()
                .map(copyMapper::toDto)
                .collect(Collectors.toList());
    }

    public CopyDto getCopyDetails(final Long id) {
        Optional<Copy> copyOptional = copyRepository.findById(id);
        return copyOptional.map(copyMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Copy with the given ID was not found.", id));
    }


    public List<CopyDto> findCurrentlyBorrowedCopies() {
        List<Copy> currentlyBorrowedCopies = copyRepository.findCurrentlyBorrowedCopies(LocalDate.now());

        return currentlyBorrowedCopies.stream()
                .map(copyMapper::toDto)
                .collect(Collectors.toList());
    }

}
