package com.example.libraryapi.service;

import com.example.libraryapi.dto.ReviewDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.exceptions.reviews.EmptyDescriptionException;
import com.example.libraryapi.exceptions.reviews.ReviewAlreadyExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Review;
import com.example.libraryapi.model.Client;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.ReviewRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final BookService bookService;
    private final ClientService clientService;

    /**
     * Adds a rating and review description to a book by a specific client.
     *
     * @param bookId      The ID of the book to rate and review.
     * @param clientId    The ID of the client adding the rating and review.
     * @param rating      The rating to be added to the book.
     * @param description The description of the review.
     * @return The created ReviewDto.
     * @throws EmptyDescriptionException    if the description is empty or null.
     * @throws ObjectNotFoundException      if the book or client with the given IDs are not found.
     * @throws ReviewAlreadyExistsException if the client has already reviewed the book.
     * @throws IllegalArgumentException     if the rating is invalid or the description fails validation.
     */
    @Transactional
    public ReviewDto addRatingToBook(final Long bookId, final Long clientId, final Integer rating, final String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyDescriptionException("Description must not be empty.");
        }

        Book book = bookService.getBookOrThrow(bookId);

        Client client = clientService.getClientOrThrow(clientId);

        hasUserReviewedBook(bookId, clientId);

        Review review = new Review();
        review.setBook(book);
        review.setUser(client);
        review.setRating(rating);
        review.setDescription(description);

        validateReviewFields(review);

        Review createdReview = reviewRepository.save(review);

        return modelMapper.map(createdReview, ReviewDto.class);
    }

    private void hasUserReviewedBook(Long bookId, Long userId) {
        boolean hasUserReviewed = reviewRepository.existsByBookIdAndUserId(bookId, userId);
        if (hasUserReviewed) {
            throw new ReviewAlreadyExistsException("User has already reviewed this book.");
        }
    }

    /**
     * Retrieves all reviews.
     *
     * @return A list of ReviewDto representing all reviews.
     */
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing review based on the provided fields.
     *
     * @param id             The identifier for the review to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     *                       Supported fields: "rating" (Integer), "description" (String).
     * @return The updated ReviewDto.
     * @throws ObjectNotFoundException  if the review with the given ID is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public ReviewDto updateReview(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Review review = getReviewOrThrow(id);

        Map<String, BiConsumer<Review, String>> fieldSetters = Map.of(
                "rating", (r, v) -> r.setRating(Integer.parseInt(v)),
                "description", Review::setDescription
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            BiConsumer<Review, String> fieldSetter = fieldSetters.get(field);
            if (fieldSetter != null) {
                fieldSetter.accept(review, value);
            } else {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }
        }

        validateReviewFields(review);

        Review updatedReview = reviewRepository.save(review);

        return modelMapper.map(updatedReview, ReviewDto.class);
    }

    private void validateReviewFields(Review review) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Review validation failed: " + violations);
        }
    }

    /**
     * Deletes a review by its identifier.
     *
     * @param id The identifier of the review to be deleted.
     */
    @Transactional
    public void deleteReview(final Long id) {
        reviewRepository.deleteById(id);
    }

    Review getReviewOrThrow(final Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ObjectNotFoundException("Review with id " + reviewId + " was not found."));
    }

}
