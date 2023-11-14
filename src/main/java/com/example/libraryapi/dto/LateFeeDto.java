package com.example.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class LateFeeDto {
    private Long id;
    private Long borrowId;
    private LocalDate date;
    private double amount;

}
