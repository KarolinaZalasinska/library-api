package com.example.libraryapi.service;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.dto.UserActivityDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.exceptions.copies.CopyNotAvailableException;
import com.example.libraryapi.model.*;
import com.example.libraryapi.repository.BorrowRepository;
import com.example.libraryapi.repository.CopyRepository;
import com.example.libraryapi.repository.UserActivityRepository;
import com.example.libraryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void borrowCopy(Long copyId, Long userId) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with ID " + copyId + " was not found."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with ID " + userId + " was not found."));

        LocalDate currentDate = LocalDate.now();

        if (!isCopyAvailable(copyId)) {
            throw new CopyNotAvailableException("The copy is not available for borrowing.", copyId);
        }

        Borrow borrow = createBorrowRecord(copy, user, currentDate);
        copy.setBorrowDate(currentDate);
        updateCopyAndSave(copy, borrow);
        updateCopyStatus(copy, CopyStatus.BORROWED);
        logUserActivity(user, copy, ActionType.BORROW, currentDate, null);
    }

    private Borrow createBorrowRecord(Copy copy, User user, LocalDate currentDate) {
        LocalDate expectedReturnDate = currentDate.plusDays(30);
        Borrow borrow = new Borrow();
        borrow.setCopy(copy);
        borrow.setUser(user);
        borrow.setBorrowDate(currentDate);
        borrow.setReturnDate(expectedReturnDate);
        return borrow;
    }

    private void updateCopyAndSave(Copy copy, Borrow borrow) {
        borrowRepository.save(borrow);
        copyRepository.save(copy);
    }

    public boolean isCopyAvailable(final Long id) {
        Optional<Copy> optionalCopy = copyRepository.findById(id);

        if (optionalCopy.isEmpty()) {
            throw new ObjectNotFoundException("Copy with ID " + id + " was not found.");
        }

        Copy copy = optionalCopy.get();
        LocalDate currentDate = LocalDate.now();

        if (copy.getReturnDate() != null && !copy.getReturnDate().isBefore(currentDate)) {
            throw new CopyNotAvailableException("The copy is not available for borrowing.", id);
        }

        return true;
    }

    @Transactional
    public void returnCopy(Long copyId, Long userId) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with ID " + copyId + " was not found."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with ID " + userId + " was not found."));

        LocalDate currentDate = LocalDate.now();

        Borrow borrow = new Borrow();
        borrow.setCopy(copy);
        borrow.setUser(user);
        borrow.setBorrowDate(currentDate);

        updateBorrowAndCopy(borrow, copy, currentDate);
        updateCopyStatus(copy, CopyStatus.AVAILABLE);
        logUserActivity(user, copy, ActionType.RETURN, borrow.getBorrowDate(), currentDate);
    }

    private void updateBorrowAndCopy(Borrow borrow, Copy copy, LocalDate returnDate) {
        borrow.setReturnDate(returnDate);
        copy.setExpectedReturnDate(returnDate);
        borrowRepository.save(borrow);
        copyRepository.save(copy);
    }

    private void logUserActivity(User user, Copy copy, ActionType actionType, LocalDate borrowDate, LocalDate returnDate) {
        UserActivity userActivity = new UserActivity();
        userActivity.setUser(user);
        userActivity.setCopy(copy);
        userActivity.setActionType(actionType);
        userActivity.setBorrowDate(borrowDate);

        if (ActionType.RETURN.equals(actionType)) {
            userActivity.setReturnDate(returnDate);
        } else {
            userActivity.setReturnDate(null);
        }
        userActivityRepository.save(userActivity);
    }

    public void updateCopyStatus(Copy copy, CopyStatus newStatus) {
        copy.setStatus(newStatus);
        copyRepository.save(copy);
    }

    public List<UserActivityDto> getBorrowHistoryForUser(Long userId) {
        List<Borrow> borrowHistoryForUser = borrowRepository.findBorrowHistoryByUserId(userId);

        if (borrowHistoryForUser.isEmpty()) {
            return Collections.emptyList();
        }
        return borrowHistoryForUser.stream()
                .map(borrow -> modelMapper.map(borrow, UserActivityDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getCurrentBorrowedCopiesForUser(Long userId) {
        List<Copy> userBorrowed = borrowRepository.findCurrentlyBorrowedCopiesForUser(userId);

        if (userBorrowed.isEmpty()) {
            return Collections.emptyList();
        }

        return userBorrowed.stream()
                .filter(borrow -> borrow.getReturnDate() == null)
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

}