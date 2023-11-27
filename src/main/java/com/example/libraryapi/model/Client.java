package com.example.libraryapi.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User's first name cannot be empty.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "User's last name cannot be empty.")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{10,}$",
            message = "Password must be at least 10 characters long, including at least one uppercase letter, one digit, and one special character [@#$%^&+=!]."
    )
    @NotBlank(message = "Password is required.")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClientActivity> userActivities;

    @OneToMany(mappedBy = "user")
    private List<Copy> copies;

}
