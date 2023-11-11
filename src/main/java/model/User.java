package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imię jest wymagane.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane.")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Nieprawidłowy adres email.")
    @NotBlank(message = "Email jest wymagany.")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{10,}$",
            message = "Hasło musi zawierać co najmniej 10 znaków, w tym co najmniej jedną wielką literę, jedną cyfrę i jeden znak specjalny [@#$%^&+=!]."
    )
    @NotBlank(message = "Hasło jest wymagane.")
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserActivity> userActivities;

}
