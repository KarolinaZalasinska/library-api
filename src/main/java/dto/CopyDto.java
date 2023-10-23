package dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CopyDto {
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO) // Czy copyNumber powinno być kluczem głównym?????????????
    private Integer copyNumber;

    @NotNull(message = "Data zakupu jest wymagana.")
    private LocalDate purchaseDate;

    private BookDto book;
}
