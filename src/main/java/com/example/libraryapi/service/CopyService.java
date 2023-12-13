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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CopyService {
    private final CopyRepository copyRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CopyDto createCopy(@Valid CopyDto copyDto) {
        Copy newCopy = copyRepository.save(modelMapper.map(copyDto, Copy.class));
        return modelMapper.map(newCopy, CopyDto.class);
    }

    public CopyDto getCopyById(final Long id) {
        return copyRepository.findById(id)
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + id + " was not found."));
    }

    public List<CopyDto> getAllCopies() {
        return copyRepository.findAll().stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public CopyDto updateCopy(final Long id, @Valid CopyDto copyDto) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + id + " was not found."));

        modelMapper.map(copyDto, copy);
        Copy updatedCopy = copyRepository.save(copy);

        return modelMapper.map(updatedCopy, CopyDto.class);
    }

    @Transactional
    public void deleteCopy(final Long id) {
        copyRepository.deleteById(id);
    }

    public List<CopyDto> getCopiesForBook(final Long bookId) {
        return copyRepository.findByBookId(bookId).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public Set<CopyDto> getAvailableCopiesNow() {
        return copyRepository.findAvailableCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toSet());
    }


    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        return copyRepository.findAvailableCopiesForBook(bookId, LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getBorrowedCopiesForClient(final Long clientId) {
        return copyRepository.findBorrowedCopiesByClientId(clientId).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getOverdueCopies() {
        return copyRepository.findOverdueCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    public CopyDto getCopyDetails(final Long id) {
        return copyRepository.findById(id)
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + id + " was not found."));
    }

    public List<CopyDto> getCurrentlyBorrowedCopies() {
        return copyRepository.findCurrentlyBorrowedCopies(LocalDate.now()).stream()
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }
}
