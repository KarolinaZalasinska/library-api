package com.example.libraryapi.repository;

import com.example.libraryapi.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    Optional<Borrow> findActiveBorrow(Long copyId, Long userId);

    List<Borrow> findBorrowHistoryByUserId(Long userId);

//    @Query("SELECT b FROM Borrow b WHERE b.user.id = :userId")
//    List<Borrow> findBorrowHistoryByUserId(@Param("userId") Long userId);
}
