package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tytuł jest wymagany")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Autor jest wymagany")
    @Column(nullable = false)
    private String author;

    @NotNull(message = "Data wydania jest wymagana")
    @PastOrPresent(message = "Data wydania nie może być przyszłością")
    @Column(nullable = false)
    private LocalDate releaseDate;

    @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$", message = "Nieprawidłowy numer ISBN.")
    @Column(nullable = false)
    private String isbn;

    @Enumerated(EnumType.STRING)
    private CopyStatus availability;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relacja jeden do wielu z egzemplarzami
    @OneToMany(mappedBy = "book")
    private List<Copy> copies = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Borrow> loans = new ArrayList<>();

    // Relacja wiele do jednego z wydawnictwem
    @ManyToOne
    private Publisher publisher;

    // Relacja wiele do wielu z autorami
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    // Relacja wiele do wielu z bibliotekami
    @ManyToMany
    @JoinTable(
            name = "book_library",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id"))
    private Set<Library> libraries = new HashSet<>();
}


