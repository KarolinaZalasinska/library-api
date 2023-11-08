package service;

import dto.CopyDto;
import exception.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.CopyMapper;
import model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CopyService {
    public final CopyRepository repository;
    public final CopyMapper mapper;

    @Transactional
    public CopyDto createCopy(final CopyDto copyDto) {
        Copy copy = mapper.toEntity(copyDto);
        Copy savedCopy = repository.save(copy);
        return mapper.toDto(savedCopy);
    }

    public CopyDto getCopyById(final Long id) {
        Optional<Copy> optionalCopy = repository.findById(id);
        return optionalCopy.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The copy with the given ID could not be found.", id));
    }

    public List<CopyDto> getAllCopies() {
        List<Copy> copies = repository.findAll();
        if (copies.isEmpty()) {
            return Collections.emptyList();
        } else {
            return copies.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CopyDto updateCopy(final Long id, CopyDto copyDto) {
        return repository.findById(id)
                .map(copy -> {
                    mapper.updateEntityFromDto(copyDto, copy);
                    Copy updatedCopy = repository.save(copy);
                    return mapper.toDto(updatedCopy);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update the copy with the given ID.", id));
    }

    @Transactional
    public void deleteCopy(final Long id) {
        repository.deleteById(id);
    }

    public List<CopyDto> getCopiesForBook(final Long bookId) {
        List<Copy> copies = repository.findByBookId(bookId);
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getAvailableCopiesForBook(final Long bookId) {
        List<Copy> copies = repository.findAvailableCopiesForBook(bookId, LocalDate.now());
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    public List<CopyDto> getBorrowedCopiesForUser(final Long userId) {
        List<Copy> copies = repository.findBorrowedCopiesForUser(userId);
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CopyDto> getOverdueCopies() {
        List<Copy> copies = repository.findOverdueCopies(LocalDate.now());
        return copies.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
