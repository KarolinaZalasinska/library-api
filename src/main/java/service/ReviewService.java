package service;

import dto.ReviewDto;
import exceptions.ObjectNotFoundInRepositoryException;
import exceptions.reviews.EmptyDescriptionException;
import exceptions.reviews.InvalidRatingException;
import exceptions.reviews.ReviewAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import mapper.ReviewMapper;
import model.Book;
import model.Review;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BookRepository;
import repository.ReviewRepository;
import repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
            return Collections.emptyList();
        } else {
            return reviews.stream()
                    .map(reviewMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ReviewDto updateReview(final Long id, ReviewDto reviewDto) {
        return reviewRepository.findById(id)
                .map(review -> {
                    reviewMapper.updateEntityFromDto(reviewDto, review);
                    Review updatedReview = reviewRepository.save(review);
                    return reviewMapper.toDto(updatedReview);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Review with the given ID was not found.", id));
    }

    @Transactional
    public ReviewDto addRatingToBook(final Long bookId, final Long userId, final Integer rating, final String description) {
        validateInput(rating,description);

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
        return reviewMapper.toDto(createdReview);
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

    // Zapytanie właśne (Query) lub metoda w repo filtrująca review po najwyższej i najniższej ocenie.
}
