package com.example.libraryapi.service;

import com.example.libraryapi.dto.ReviewDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import com.example.libraryapi.exceptions.reviews.EmptyDescriptionException;
import com.example.libraryapi.exceptions.reviews.InvalidRatingException;
import com.example.libraryapi.exceptions.reviews.ReviewAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Review;
import com.example.libraryapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.BookRepository;
import com.example.libraryapi.repository.ReviewRepository;
import com.example.libraryapi.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
            return Collections.emptyList();
        } else {
            return reviews.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ReviewDto updateReview(final Long id, @Valid final ReviewDto reviewDto) {
        return reviewRepository.findById(id)
                .map(review -> {
                    updateEntityFromDto(reviewDto, review);
                    Review updatedReview = reviewRepository.save(review);
                    return convertToDto(updatedReview);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Review with the given ID was not found.", id));
    }

    @Transactional
    public ReviewDto addRatingToBook(final Long bookId, final Long userId, final Integer rating, final String description) {
        validateInput(rating, description);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Book with the given ID was not found.", bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("User with the given ID was not found.", userId));

        boolean hasUserReviewed = reviewRepository.existsByBookIdAndUserId(bookId, userId);
        if (hasUserReviewed) {
            throw new ReviewAlreadyExistsException("User has already reviewed this book.");
        }

        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setRating(rating);
        review.setDescription(description);

        Review createdReview = reviewRepository.save(review);
        return convertToDto(createdReview); // ???????????????????????????????????????????
    }

    private void validateInput(final Integer rating, final String description) {
        if (rating == null || rating < 1 || rating > 6) {
            throw new InvalidRatingException("Invalid rating value. The rating must be between 1 and 6.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyDescriptionException("Description must not be empty.");
        }
    }

    @Transactional
    public void deleteReview(final Long id) {
        reviewRepository.deleteById(id);
    }

    // Query/metoda w repo filtrująca review po najwyższej i najniższej ocenie.
    // Dodaj ewentualnie adnotacje @Valid gdzie uznasz to za słuszne.

    private ReviewDto convertToDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    private void updateEntityFromDto(ReviewDto reviewDto, Review review) {
        // Aktualizuj pola z reviewDto do obiektu review
        modelMapper.map(reviewDto, review);
    }
    // Query/metoda w repo filtrująca review po najwyższej i najniższej ocenie.
}
