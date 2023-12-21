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
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Purchase date must be in the past or present.")
    @Column(nullable = false)
    private LocalDate purchaseDate;

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
    private LocalDate returnDate;

    @OneToMany(mappedBy = "copy")
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "copy")
    private List<ClientActivity> userActivities;

    @ManyToOne
    private Book book;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private CopyStatus copyStatus;

    public void setStatus(CopyStatus newStatus) {
        this.copyStatus = newStatus;
    }

    public CopyStatus getStatus() {
        return copyStatus;
    }
}