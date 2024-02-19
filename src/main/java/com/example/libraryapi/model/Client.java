package com.example.libraryapi.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Client's first name cannot be empty.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Client's last name cannot be empty.")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "client")
    private List<Borrow> borrows = new ArrayList<>();

    @OneToMany(mappedBy = "client")
    private List<ClientActivity> clientActivities;

    @OneToMany(mappedBy = "client")
    private List<Copy> copies;

}
