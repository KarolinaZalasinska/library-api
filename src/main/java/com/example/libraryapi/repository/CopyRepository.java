package com.example.libraryapi.repository;

import com.example.libraryapi.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {
    List<Copy> findByBookId(Long bookId);
    @Query("SELECT c FROM Copy c WHERE c.borrower IS NULL OR c.availabilityDate > :now")
    List<Copy> findAvailableCopies(@Param("now") LocalDate now);
    @Query("SELECT c FROM Copy as c WHERE c.book.id = :bookId AND (c.borrowedDate IS NULL OR c.borrowedDate < :currentDate) AND (c.returnDate IS NULL OR c.returnDate < :currentDate)")
    List<Copy> findAvailableCopiesForBook(@Param("bookId") Long bookId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT c FROM Copy c WHERE c.borrowedDate IS NOT NULL AND c.returnDate IS NULL AND c.expectedReturnDate < :currentDate")
    List<Copy> findOverdueCopies(@Param("currentDate") LocalDate currentDate);

    List<Copy> findBorrowedCopiesByUserId(Long userId);



    @Query("SELECT c FROM Copy c WHERE c.borrowedDate IS NOT NULL AND c.returnDate IS NULL AND c.expectedReturnDate < :currentDate")
   List<Copy> findCurrentlyBorrowedCopies(@Param("currentDate") LocalDate currentDate);

}
