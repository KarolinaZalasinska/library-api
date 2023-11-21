package com.example.libraryapi.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Purchase date is required.")
    @Column(nullable = false)
    private LocalDate purchaseDate;

    @PastOrPresent(message = "Borrow date must be in the past or present.")
    private LocalDate borrowDate;

    @Future(message = "Expected return date must be in the future.")
    private LocalDate expectedReturnDate;

    @PastOrPresent(message = "Return date must be in the past or present.")
    private LocalDate returnDate;

    @OneToMany(mappedBy = "copy")
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "copy")
    private List<UserActivity> userActivities;

    @ManyToOne
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setStatus(CopyStatus newStatus) {
    }

}
