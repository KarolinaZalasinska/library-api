package com.example.libraryapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
public record LateFeeDto(
        Long id,
        LocalDate dateOfLateFee,
        BigDecimal amount,
        Long borrowId
) {
}
