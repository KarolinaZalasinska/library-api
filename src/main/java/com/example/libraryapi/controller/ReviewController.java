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

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(value = "Review Management System", tags = "{ Review }")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    @ApiOperation(value = "Add rating to book")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name())")
    public ResponseEntity<ReviewDto> addRatingToBook(
            @ApiParam(value = "Book id", required = true) @RequestParam final Long bookId,
            @ApiParam(value = "User id", required = true) @RequestParam final Long userId,
            @Min(1) @Max(6) @RequestParam final Integer rating,
            @ApiParam(value = "Review description", required = true) @NotBlank @RequestParam final String description) {
        ReviewDto addedReview = service.addRatingToBook(bookId, userId, rating, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
    }

    @GetMapping
    @ApiOperation(value = "Get all reviews")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = service.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update review by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name())")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable @ApiParam(value = "Review id", required = true) final Long id,
            @RequestBody @ApiParam(value = "Updated review data", required = true) @Valid ReviewDto reviewDto) {
        ReviewDto updatedReview = service.updateReview(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete review by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).USER.name())")
    public ResponseEntity<Void> deleteReview(
            @PathVariable @ApiParam(value = "Review id", required = true) final Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
