package com.example.libraryapi.dto;

import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BorrowDto(
        Long id,

        @CreationTimestamp
        LocalDateTime borrowDate,

        @CreationTimestamp
        LocalDateTime expectedReturnDate,

        @PastOrPresent(message = "Return date must be in the past or present.")
        LocalDate returnDate,

        List<LateFeeDto> lateFees,

        BookDto book,

        CopyDto copy,

        ClientDto user

) {
}
