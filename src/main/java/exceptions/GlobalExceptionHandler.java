package exceptions;

import exceptions.copies.CopyNotAvailableException;
import exceptions.reviews.EmptyDescriptionException;
import exceptions.reviews.InvalidRatingException;
import exceptions.reviews.ReviewAlreadyExistsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotFoundInRepositoryException.class)
    public ResponseEntity<Object> handleObjectNotFoundInRepository(ObjectNotFoundInRepositoryException e) {
        Long id = e.getId();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Object not found with ID " + id);
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
    public ResponseEntity<String> handleCopyNotAvailableException(CopyNotAvailableException e) {
        Long copyId = e.getCopyId();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Copy not available for copyId: " + copyId);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleRuntimeException(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
