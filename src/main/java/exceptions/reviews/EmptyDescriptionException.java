package exceptions.reviews;

public class EmptyDescriptionException extends RuntimeException {
    public EmptyDescriptionException(String message) {
        super(message);
    }
}
