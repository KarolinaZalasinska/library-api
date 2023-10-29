package exception;

public class ObjectNotFoundInRepositoryException extends RuntimeException {
    private Long id;

    public ObjectNotFoundInRepositoryException(String message, Long id) {
        super(message);
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}
