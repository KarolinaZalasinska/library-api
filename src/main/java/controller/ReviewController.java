package controller;

import dto.ReviewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ReviewService;

import java.util.List;

@Api(value = "Review Management System", tags = "{ Reviews }")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    @ApiOperation(value = "Get all reviews")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = service.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation(value = "Update a review by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable @ApiParam(value = "ID of the review", required = true) final Long id,
            @RequestBody @ApiParam(value = "Updated review data", required = true) ReviewDto reviewDto) {
        ReviewDto updatedReview = service.updateReview(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

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

    @ApiOperation(value = "Delete a review by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable @ApiParam(value = "ID of the review", required = true) final Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
