package exceptions;

import exceptions.copies.CopyNotAvailableException;
import exceptions.reviews.DuplicateBookException;
import exceptions.reviews.EmptyDescriptionException;
import exceptions.reviews.InvalidRatingException;
import exceptions.reviews.ReviewAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotFoundInRepositoryException.class)
    public ResponseEntity<Object> handleObjectNotFoundInRepository(ObjectNotFoundInRepositoryException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "No object with the given ID was found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<Object> handleReviewAlreadyExistsException(ReviewAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review already exists: " + e.getMessage());
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<Object> handleInvalidRatingException(InvalidRatingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid rating: " + e.getMessage());
    }

    @ExceptionHandler(EmptyDescriptionException.class)
    public ResponseEntity<Object> handleEmptyDescriptionException(EmptyDescriptionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty description: " + e.getMessage());
    }

    @ExceptionHandler(SomeCustomException.class)
    public ResponseEntity<Object> handleCustomException(SomeCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create: " + e.getMessage());
    }

    @ExceptionHandler(CopyNotAvailableException.class)
    public ResponseEntity<Object> handleCopyNotAvailableException(CopyNotAvailableException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "Copy is not available");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(DuplicateBookException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDuplicateBookException(DuplicateBookException exception) {
        String duplicates = exception.getDuplicates()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String errorMessage = String.format("Duplicate books found: %s", duplicates);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, errorMessage);
    }
}
