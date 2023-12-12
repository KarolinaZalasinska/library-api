package com.example.libraryapi.service;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.dto.ClientActivityDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.exceptions.copies.CopyNotAvailableException;
import com.example.libraryapi.model.*;
import com.example.libraryapi.repository.BorrowRepository;
import com.example.libraryapi.repository.CopyRepository;
import com.example.libraryapi.repository.ClientActivityRepository;
import com.example.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final CopyRepository copyRepository;
    private final ClientRepository clientRepository;
    private final ClientActivityRepository clientActivityRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void borrowCopy(Long copyId, Long clientId) {
        Copy copy = findCopyById(copyId);
        Client client = findClientById(clientId);
        LocalDate currentDate = LocalDate.now();

        if (!isCopyAvailable(copy)) {
            throw new CopyNotAvailableException("The copy is not available for borrowing.", copyId);
        }

        Borrow borrow = createBorrowRecord(copy, client, currentDate);
        copy.setBorrowDate(currentDate);
        updateCopyStatus(copy, CopyStatus.BORROWED);
        updateCopyAndSave(copy, borrow);
        logUserActivity(client, copy, ActionType.BORROW, currentDate, null);
    }

    @Transactional
    public Borrow createBorrowRecord(Copy copy, Client client, LocalDate currentDate) {
        LocalDate expectedReturnDate = currentDate.plusDays(30);
        Borrow borrow = new Borrow();
        borrow.setCopy(copy);
        borrow.setClient(client);
        borrow.setBorrowDate(currentDate);
        borrow.setReturnDate(expectedReturnDate);
        return borrowRepository.save(borrow);
    }

    private void updateCopyAndSave(Copy copy, Borrow borrow) {
        borrowRepository.save(borrow);
        copyRepository.save(copy);
    }

    public boolean isCopyAvailable(Copy copy) {
        LocalDate currentDate = LocalDate.now();
        return copy.getReturnDate() == null || copy.getReturnDate().isBefore(currentDate);
    }

    public Copy findCopyById(Long copyId) {
        return copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + copyId + " was not found."));
    }

    private Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException("Client with id " + clientId + " was not found."));
    }

    @Transactional
    public void returnCopy(Long copyId, Long clientId) {
        Copy copy = findCopyById(copyId);
        Client client = findClientById(clientId);
        Borrow activeBorrow = borrowRepository.findByCopyAndClientAndReturnDateIsNull(copy, client);

        if (activeBorrow == null) {
            throw new ObjectNotFoundException("No active borrow record found for the copy and client.");
        }

        LocalDate currentDate = LocalDate.now();
        updateBorrowAndCopy(activeBorrow, copy, currentDate);
        updateCopyStatus(copy, CopyStatus.AVAILABLE);
        logUserActivity(client, copy, ActionType.RETURN, activeBorrow.getBorrowDate(), currentDate);
    }

    private void updateBorrowAndCopy(Borrow borrow, Copy copy, LocalDate returnDate) {
        borrow.setReturnDate(returnDate);

        if (borrow.getClient().equals(copy.getClient())) {
            copy.setExpectedReturnDate(returnDate);
            borrowRepository.save(borrow);
            copyRepository.save(copy);
        } else {
            throw new IllegalArgumentException("Mismatched borrow record for the given copy and client.");
        }
    }

    @Transactional
    public void logUserActivity(Client client, Copy copy, ActionType actionType, LocalDate borrowDate, LocalDate
            returnDate) {
        ClientActivity clientActivity = new ClientActivity();
        clientActivity.setClient(client);
        clientActivity.setCopy(copy);
        clientActivity.setActionType(actionType);
        clientActivity.setBorrowDate(borrowDate);
        clientActivity.setReturnDate(returnDate);

        clientActivityRepository.save(clientActivity);
    }

    public void updateCopyStatus(Copy copy, CopyStatus newStatus) {
        copy.setStatus(newStatus);
        copyRepository.save(copy);
    }

    public List<ClientActivityDto> getBorrowHistoryForUser(Long clientId) {
        List<Borrow> borrowHistoryForUser = borrowRepository.findBorrowHistoryByClientId(clientId);

        return borrowHistoryForUser.stream()
                .map(borrow -> modelMapper.map(borrow, ClientActivityDto.class))
                .collect(Collectors.toList());
    }

    public List<CopyDto> getCurrentBorrowedCopiesForClient(Long clientId) {
        List<Copy> clientBorrowed = borrowRepository.findCurrentlyBorrowedCopiesForClient(clientId);

        return clientBorrowed.stream()
                .filter(borrow -> borrow.getReturnDate() == null)
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }
}
