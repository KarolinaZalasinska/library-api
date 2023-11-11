package dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import model.CopyStatus;

import java.time.LocalDate;

@Data
public class CopyDto {
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO) // Czy copyNumber powinno być kluczem głównym?????????????
    private Integer copyNumber;
    private LocalDate purchaseDate;
    private BookDto book;
    private CopyStatus copyStatus;
}
