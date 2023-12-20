package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title is required.")
    private String bookTitle;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @CreationTimestamp
    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @PastOrPresent(message = "Return date must be in the past or present.")
    private LocalDateTime returnDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;

}

