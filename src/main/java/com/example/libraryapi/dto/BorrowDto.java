package com.example.libraryapi.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

public record BorrowDto(
        Long id,
        @PastOrPresent(message = "Borrow date must be in the past or present.") LocalDate borrowDate,
        @Future(message = "Expected return date must be in the future") LocalDate expectedReturnDate,
        @PastOrPresent(message = "Return date must be in the past or present.") LocalDate returnDate,
        List<LateFeeDto> lateFees,
        BookDto book,
        CopyDto copy,
        ClientDto user

) {
}
