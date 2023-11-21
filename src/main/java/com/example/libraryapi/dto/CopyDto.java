package com.example.libraryapi.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

public record CopyDto(
        Long id,
        @NotNull(message = "Purchase date is required.") LocalDate purchaseDate,
        @PastOrPresent(message = "Borrow date must be in the past or present.") LocalDate borrowDate,
        @Future(message = "Expected return date must be in the future.") LocalDate expectedReturnDate,
        @PastOrPresent(message = "Return date must be in the past or present.") LocalDate returnDate,
        List<BorrowDto> borrows

) {
}
