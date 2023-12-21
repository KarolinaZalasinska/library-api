package com.example.libraryapi.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;
public record LateFeeDto(
        Long id,

        @PastOrPresent(message = "Late fee date must be in the past or present.")
        LocalDate dateOfLateFee,

        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.")
        BigDecimal amount,

        Long borrowId
) {
}
