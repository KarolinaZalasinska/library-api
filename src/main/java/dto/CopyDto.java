package dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import model.CopyStatus;

import java.time.LocalDate;

@Data
public class CopyDto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer copyNumber;
    private LocalDate purchaseDate;
    private BookDto book;
    private CopyStatus copyStatus;
}
