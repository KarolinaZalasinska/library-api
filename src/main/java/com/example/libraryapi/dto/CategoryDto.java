package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Nazwa kategorii jest wymagana.")
    private String name;

    private Set<BookDto> books; // ?????????????????????
}
