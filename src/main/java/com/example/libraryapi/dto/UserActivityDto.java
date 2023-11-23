package com.example.libraryapi.dto;

import com.example.libraryapi.model.ActionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UserActivityDto(
        Long id,

        @NotBlank(message = "Book title is required.")
        String bookTitle,

        ActionType actionType,

        @PastOrPresent(message = "Borrow date must be in the past or present.")
        LocalDate borrowDate,

        @PastOrPresent(message = "Return date must be in the past or present.")
        LocalDate returnDate,

        Long userId,
        Long copyId
) {
}
