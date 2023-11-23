package com.example.libraryapi.controller;

import com.example.libraryapi.dto.ReviewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.ReviewService;

import java.util.List;

@Api(value = "Review Management System", tags = "{ Review }")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;
    @ApiOperation(value = "Add a rating to book")
    @PostMapping
    public ResponseEntity<ReviewDto> addRatingToBook(
            @RequestParam @ApiParam(value = "ID of the book", required = true) Long bookId,
            @RequestParam @ApiParam(value = "ID of the user", required = true) Long userId,
            @RequestParam @ApiParam(value = "Rating", required = true) Integer rating,
            @RequestParam @ApiParam(value = "Review description", required = true) String description) {
        ReviewDto addedReview = service.addRatingToBook(bookId, userId, rating, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
    }
    @ApiOperation(value = "Get all reviews")
    @GetMapping
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = service.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation(value = "Update a review by id")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable @ApiParam(value = "Id of the review", required = true) final Long id,
            @RequestBody @ApiParam(value = "Updated review data", required = true) ReviewDto reviewDto) {
        ReviewDto updatedReview = service.updateReview(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }



    @ApiOperation(value = "Delete a review by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable @ApiParam(value = "Id of the review", required = true) final Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
