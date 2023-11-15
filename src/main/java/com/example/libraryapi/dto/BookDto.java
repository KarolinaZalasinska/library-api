package com.example.libraryapi.dto;

import lombok.Data;
import com.example.libraryapi.model.CopyStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class BookDto {
    private Long id;

    @NotBlank(message = "Tytuł jest wymagany")
    private String title;

    @NotBlank(message = "Autor jest wymagany")
    private String author;

    @NotNull(message = "Data wydania jest wymagana")
    @PastOrPresent(message = "Data wydania nie może być przyszłością")
    private LocalDate releaseDate;

    @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$", message = "Nieprawidłowy numer ISBN.")
    private String isbn;

    private CopyStatus availability;
//
//    private List<CopyDto> copies;
//    private List<BorrowDto> loans;
//    private PublisherDto publisher;
//    private Set<AuthorDto> authors;
//    private Set<CategoryDto> bookCategories;
//    private Set<LibraryDto> libraries;
}
