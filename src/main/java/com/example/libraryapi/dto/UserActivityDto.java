package com.example.libraryapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UserActivityDto(
        Long id,

        @NotBlank(message = "Book title cannot be blank")
        String bookTitle,

        @NotBlank(message = "Action type cannot be blank")
        String actionType,

        @NotBlank(message = "Action borrow cannot be blank")
        String actionBorrow,

        @NotBlank(message = "Action return cannot be blank")
        String actionReturn,

        @NotNull(message = "Borrow date cannot be null") @PastOrPresent(message = "Borrow date must be in the past or present")
        LocalDate borrowDate,

        LocalDate returnDate,

        @NotNull(message = "User ID cannot be null") Long userId,

        @NotNull(message = "Copy ID cannot be null") Long copyId
) {
}
