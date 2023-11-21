package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LateFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Late fee date must be in the past or present.")
    private LocalDate lateFeeDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "borrow_id")
    private Borrow borrow;
}
