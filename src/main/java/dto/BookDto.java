package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import model.Availability;
import model.Copy;
import model.Loan;
import model.Publisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    private Availability availability;

    private List<CopyDto> copies;
    private List<LoanDto> loans;
    private PublisherDto publisher;
    private Set<AuthorDto> authors;
    private Set<CategoryDto> bookCategories;
    private Set<LibraryDto> libraries;
}
