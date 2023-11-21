package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title is required.")
    private String bookTitle;

    private String actionType;
    private String actionBorrow;
    private String actionReturn;

    @NotNull(message = "Borrow date cannot be null.")
    @PastOrPresent(message = "Borrow date must be in the past or present.")
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;

}
