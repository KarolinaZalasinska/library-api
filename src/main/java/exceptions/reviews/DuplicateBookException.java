package exceptions.reviews;

import model.Book;

import java.util.List;

public class DuplicateBookException extends RuntimeException{
    private List<Book> duplicates;

    public DuplicateBookException(String message, List<Book> duplicates) {
        super(message);
        this.duplicates = duplicates;
    }

    public List<Book> getDuplicates() {
        return duplicates;
    }
}
