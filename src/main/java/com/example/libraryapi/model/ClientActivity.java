package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

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

    @PastOrPresent(message = "Borrow date must be in the past or present.")
    private LocalDate borrowDate;

    @PastOrPresent(message = "Return date must be in the past or present.")
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;

}
