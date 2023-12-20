package com.example.libraryapi.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

public record CopyDto(
        Long id,
        @PastOrPresent(message = "Purchase date must be in the past or present.") LocalDate purchaseDate,
        @PastOrPresent(message = "Borrow date must be in the past or present.") LocalDate borrowDate,
        @Future(message = "Expected return date must be in the future.") LocalDate expectedReturnDate,
        @PastOrPresent(message = "Return date must be in the past or present.") LocalDate returnDate,
        List<BorrowDto> borrows

) {
}
