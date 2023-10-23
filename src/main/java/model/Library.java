package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa biblioteki jest wymagana.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Adres jest wymagany.")
    @Column(nullable = false)
    private String address;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Nieprawidłowy format kodu pocztowego.")
    @Column(nullable = false, length = 6)
    private char postalCode;

    @ManyToMany(mappedBy = "libraries")
    private Set<Book> books = new HashSet<>();
}
