package service;

import dto.ReviewDto;
import exception.ObjectNotFoundInRepositoryException;
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
    public ReviewDto addRatingToBook(final Long bookId, final Long userId, final Integer rating) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The book with the given ID was not found.", bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The user with the given ID was not found.", userId));

        // Sprawdź, czy użytkownik już ocenił tę książkę
        boolean hasUserReviewed = reviewRepository.existsByBookIdAndUserId(bookId, userId);

        if (hasUserReviewed) {
            throw new ReviewAlreadyExistsException("User has already reviewed this book.");
        }

        // Sprawdź, czy ocena mieści się w zakresie od 1 do 6
        if (rating < 1 || rating > 6) {
            throw new InvalidRatingException("Invalid rating value. The rating must be between 1 and 6.");
        }

        // Utwórz nową recenzję
        Review review = new Review();
        review.setBook(book);
        review.setUser(user);
        review.setRating(rating);

        // Zapisz recenzję
        Review createdReview = reviewRepository.save(review);

        // Zwróć DTO recenzji
        return reviewMapper.toDto(createdReview);
    }

    @Transactional
    public ReviewDto addReviewToBook(final Long bookId, final Long userId, final String description) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The book with the given ID was not found.", bookId));


    }
}
