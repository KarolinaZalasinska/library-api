package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ocena jest wymagana")
    @Min(value = 1, message = "Ocena musi być większa lub równa 1")
    @Max(value = 6, message = "Ocena musi być mniejsza lub równa 6")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Pole 'Opis' jest wymagane")
    @Size(max = 50,message = "Opis nie może przekraczać 50 znaków.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relacja wiele do jednego (Many-to-One) z książką
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    // Relacja wiele do jednego (Many-to-One) z użytkownikiem, który stworzył recenzję
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
