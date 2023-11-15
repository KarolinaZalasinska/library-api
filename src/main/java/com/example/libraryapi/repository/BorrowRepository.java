package com.example.libraryapi.repository;

import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findBorrowHistoryByUserId(Long userId);
    @Query("SELECT b.copy FROM Borrow b WHERE b.user.id = :userId AND b.returnDate IS NULL")
    List<Copy> findCurrentlyBorrowedCopiesForUser(@Param("userId") Long userId);

}
