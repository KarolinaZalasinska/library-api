package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer copyNumber;

    @NotNull(message = "Data zakupu jest wymagana.")
    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private LocalDate dateOfBorrow;

    @Column(nullable = false)
    private LocalDate expectedReturnDate;

    private LocalDate borrowedDate;

    private LocalDate returnDate;

    @OneToMany(mappedBy = "copy")
    private List<Borrow> loans = new ArrayList<>();

    @OneToMany(mappedBy = "copy")
    private List<UserActivity>userActivities;

    @ManyToOne
    private Book book;
}
