package com.example.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "expected_return_date")
    @CreationTimestamp
    private LocalDateTime expectedReturnDate;

    @PrePersist
    public void prePersist() {
        this.expectedReturnDate = LocalDateTime.now().plusDays(30);
    }

    @PastOrPresent(message = "Return date must be in the past or present.")
    private LocalDateTime returnDate;

    @OneToMany(mappedBy = "borrow")
    private List<LateFee> lateFees;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;


}
