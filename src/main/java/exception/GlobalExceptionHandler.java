package exception;

import jdk.jshell.Snippet;
import org.springframework.beans.factory.parsing.Problem;
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

    @ExceptionHandler(SomeCustomException.class)
    public ResponseEntity<Object> handleCustomException(SomeCustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleRuntimeException(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
