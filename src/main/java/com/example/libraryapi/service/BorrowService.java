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

/**
 * Service class for managing borrows, copies, and client activities.
 */
@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final CopyRepository copyRepository;
    private final ClientRepository clientRepository;
    private final ClientActivityRepository clientActivityRepository;
    private final ModelMapper modelMapper;

    /**
     * Borrow a copy for a given client.
     *
     * @param copyId   The identifier of the copy to be borrowed.
     * @param clientId The identifier of the client borrowing the copy.
     * @throws ObjectNotFoundException   if either the copy or the client is not found.
     * @throws CopyNotAvailableException if the copy is not available for borrowing.
     */
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

    /**
     * Create a borrow record for a given copy, client, and current date.
     *
     * @param copy        The copy being borrowed.
     * @param client      The client borrowing the copy.
     * @param currentDate The current date.
     * @return The created Borrow record.
     */
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

    /**
     * Check if a copy is available for borrowing.
     *
     * @param copy The copy to be checked.
     * @return True if the copy is available, false otherwise.
     */
    public boolean isCopyAvailable(Copy copy) {
        LocalDate currentDate = LocalDate.now();
        return copy.getReturnDate() == null || copy.getReturnDate().isBefore(currentDate);
    }

    /**
     * Return a borrowed copy for a given client.
     *
     * @param copyId   The identifier of the copy to be returned.
     * @param clientId The identifier of the client returning the copy.
     * @throws ObjectNotFoundException if no active borrow record is found for the copy and client.
     */
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

    /**
     * Update the borrow and copy records when a copy is returned.
     *
     * @param borrow     The Borrow record being updated.
     * @param copy       The Copy being returned.
     * @param returnDate The date of returning the copy.
     * @throws IllegalArgumentException if there is a mismatch in the borrow record for the given copy and client.
     */
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

    /**
     * Log user activity such as borrow or return.
     *
     * @param client     The client involved in the activity.
     * @param copy       The copy involved in the activity.
     * @param actionType The type of action (BORROW or RETURN).
     * @param borrowDate The date of borrowing.
     * @param returnDate The date of returning.
     */
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

    /**
     * Update the status of a copy.
     *
     * @param copy      The copy to be updated.
     * @param newStatus The new status for the copy.
     */
    public void updateCopyStatus(Copy copy, CopyStatus newStatus) {
        copy.setStatus(newStatus);
        copyRepository.save(copy);
    }

    /**
     * Get the borrow history for a specific client.
     *
     * @param clientId The identifier of the client.
     * @return A list of ClientActivityDto representing the borrow history for the client.
     */
    public List<ClientActivityDto> getBorrowHistoryForClient(Long clientId) {
        List<Borrow> borrowHistoryForClient = borrowRepository.findBorrowHistoryByClientId(clientId);

        return borrowHistoryForClient.stream()
                .map(borrow -> modelMapper.map(borrow, ClientActivityDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Get currently borrowed copies for a specific client.
     *
     * @param clientId The identifier of the client.
     * @return A list of CopyDto representing currently borrowed copies for the client.
     */
    public List<CopyDto> getCurrentBorrowedCopiesForClient(Long clientId) {
        List<Copy> clientBorrowed = borrowRepository.findCurrentlyBorrowedCopiesForClient(clientId);

        return clientBorrowed.stream()
                .filter(borrow -> borrow.getReturnDate() == null)
                .map(copy -> modelMapper.map(copy, CopyDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a copy by its identifier.
     *
     * @param copyId The identifier of the copy.
     * @return The Copy associated with the given copyId.
     * @throws ObjectNotFoundException if the copy is not found.
     */
    public Copy findCopyById(Long copyId) {
        return copyRepository.findById(copyId)
                .orElseThrow(() -> new ObjectNotFoundException("Copy with id " + copyId + " was not found."));
    }

    Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException("Client with id " + clientId + " was not found."));
    }
}
