package com.example.libraryapi.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Publisher name is required.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Publisher address is required.")
    @Column(nullable = false)
    private String address;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postal code format.")
    @Column(nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "publisher")
    private Set<Book> books = new HashSet<>();
}
