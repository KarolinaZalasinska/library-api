package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
public class LibraryDto {
    private Long id;

    @NotBlank(message = "Nazwa biblioteki jest wymagana.")
    private String name;

    @NotBlank(message = "Adres jest wymagany.")
    private String address;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Nieprawidłowy format kodu pocztowego.")
    private String postalCode;

    private Set<BookDto> books; // ????????????????????????????
}
