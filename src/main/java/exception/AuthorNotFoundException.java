package exception;

public class AuthorNotFoundException extends LibraryAppException{
    public AuthorNotFoundException(Long id) {
        super("Nie znaleziono autora o identyfikatorze " + id);
    }
}
