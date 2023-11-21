package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.optional.qual.Present;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Borrow date must be in the past or present.")
    private LocalDate borrowDate;

    @Future(message = "Expected return date must be in the future.")
    private LocalDate expectedReturnDate;

    @PastOrPresent(message = "Return date must be in the past or present.")
    private LocalDate returnDate;

    @OneToMany(mappedBy = "borrow")
    private List<LateFee> lateFees;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;


}
