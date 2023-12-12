package com.example.libraryapi.repository;

import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.Client;
import com.example.libraryapi.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findBorrowHistoryByClientId(Long clientId);

    @Query("SELECT b.copy FROM Borrow b WHERE b.client.id = :clientId AND b.returnDate IS NULL")
    List<Copy> findCurrentlyBorrowedCopiesForClient(@Param("clientId") Long clientId);

    Borrow findByCopyAndClientAndReturnDateIsNull(Copy copy, Client client);
}
