package com.example.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Nazwa kategorii jest wymagana.")
    private String name;

    private Set<BookDto> books; // ?????????????????????
}
