package com.example.libraryapi.dto;

import com.example.libraryapi.model.CopyStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record BookDto(
        Long id,
        @NotBlank(message = "Title is required.") String title,
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$", message = "Invalid ISBN.") String isbn,
        CopyStatus availability,
        Set<Long> authorIds,
        List<Long> copyIds,
        List<Long> borrowIds,
        Long publisherId,
        Set<Long> categoryIds,
        Set<Long> libraryIds

) {
}
