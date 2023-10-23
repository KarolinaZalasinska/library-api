package dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;

    @NotNull(message = "Ocena jest wymagana.")
    @Min(value = 1, message = "Ocena musi być większa lub równa 1.")
    @Max(value = 6, message = "Ocena musi być mniejsza lub równa 6.")
    private Integer rating;

    @NotBlank(message = "Pole 'Opis' jest wymagane.")
    @Size(max = 50,message = "Opis nie może przekraczać 50 znaków.")
    private String description;

    private Long bookId;

    private Long userId;
}
