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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CopyService {
    private final CopyRepository copyRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CopyDto createCopy(@Valid CopyDto copyDto) {
        Copy copy = modelMapper.map(copyDto, Copy.class);
        Copy savedCopy = copyRepository.save(copy);
        return modelMapper.map(savedCopy, CopyDto.class);
    }

    public CopyDto getCopyById(final Long id) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);
        return optionalCopy.map(copy -> modelMapper.map(copy, CopyDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Copy with ID " + id + " was not found."));
    }

    public List<CopyDto> getAllCopies() {
        List<Copy> copies = copyRepository.findAll();
        if (copies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return copies.stream()
                    .map(copy -> modelMapper.map(copy, CopyDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CopyDto updateCopy(final Long id, @Valid CopyDto copyDto) {
        return copyRepository.findById(id)
                .map(copy -> {
                    modelMapper.map(copyDto, copy);
                    Copy updatedCopy = copyRepository.save(copy);
                    return modelMapper.map(updatedCopy, CopyDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("Copy with ID " + id + " was not found."));
    }

    @Transactional
    public void deleteCopy(final Long id) {
        copyRepository.deleteById(id);
    }

    public List<CopyDto> getCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findByBookId(bookId);
        return copies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getAvailableCopiesNow() {
        List<Copy> availableCopies = copyRepository.findAvailableCopies(LocalDate.now());
        return availableCopies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        List<Copy> copies = copyRepository.findAvailableCopiesForBook(bookId, LocalDate.now());
        return copies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getBorrowedCopiesForUser(final Long userId) {
        List<Copy> copies = copyRepository.findBorrowedCopiesByUserId(userId);
        return copies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getOverdueCopies() {
        List<Copy> copies = copyRepository.findOverdueCopies(LocalDate.now());
        return copies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public CopyDto getCopyDetails(final Long id) {
        Optional<Copy> copyOptional = copyRepository.findById(id);
        return copyOptional.map(copy -> modelMapper.map(copy, CopyDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Copy with ID " + id + " was not found."));
    }

    public List<CopyDto> getCurrentlyBorrowedCopies() {
        List<Copy> currentlyBorrowedCopies = copyRepository.findCurrentlyBorrowedCopies(LocalDate.now());
        return currentlyBorrowedCopies.stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }
}
