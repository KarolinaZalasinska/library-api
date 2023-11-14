package com.example.libraryapi.repository;

import com.example.libraryapi.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}
