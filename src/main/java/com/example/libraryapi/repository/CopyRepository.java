package com.example.libraryapi.repository;

import com.example.libraryapi.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface CopyRepository extends JpaRepository<Copy, Long> {
    List<Copy> findByBookId(Long bookId);
    @Query("SELECT c FROM Copy c WHERE c.user IS NULL OR c.expectedReturnDate > :now")
    List<Copy> findAvailableCopies(@Param("now") LocalDate now);

    @Query("SELECT c FROM Copy as c WHERE c.book.id = :bookId AND (c.borrowDate IS NULL OR c.borrowDate < :currentDate) AND (c.returnDate IS NULL OR c.returnDate < :currentDate)")
    List<Copy> findAvailableCopiesForBook(@Param("bookId") Long bookId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT c FROM Copy c WHERE c.borrowDate IS NOT NULL AND c.returnDate IS NULL AND c.expectedReturnDate < :currentDate")
    List<Copy> findOverdueCopies(@Param("currentDate") LocalDate currentDate);

    List<Copy> findBorrowedCopiesByUserId(Long userId);
    @Query("SELECT c FROM Copy c WHERE c.borrowDate IS NOT NULL AND c.returnDate IS NULL AND c.expectedReturnDate < :currentDate")
   List<Copy> findCurrentlyBorrowedCopies(@Param("currentDate") LocalDate currentDate);
}
