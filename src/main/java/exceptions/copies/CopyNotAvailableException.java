package exceptions.copies;

import lombok.Getter;

@Getter
public class CopyNotAvailableException extends RuntimeException {
    private final Long copyId;

    public CopyNotAvailableException(String message, Long copyId) {
        super(message);
        this.copyId = copyId;
    }

}
