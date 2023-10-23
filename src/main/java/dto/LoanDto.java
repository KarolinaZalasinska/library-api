package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    private Long id;
    private LocalDate dateOfBorrow;
    private LocalDate plannedReturnDate;
    private Long bookId;
    private Long userId;
}