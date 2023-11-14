package com.example.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Set;

@Data
public class PublisherDto {
    private Long id;

    @NotBlank(message = "Nazwa wydawnictwa jest wymagana.")
    private String name;

    @NotBlank(message = "Adres jest wymagany.")
    private String address;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Nieprawidłowy format kodu pocztowego.")
    private String postalCode;

    private Set<BookDto> books;  // ???????????????
}
