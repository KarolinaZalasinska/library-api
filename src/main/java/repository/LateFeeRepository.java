package repository;

import model.LateFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee, Long> {
    // Metoda do pobrania wszystkich opłat za spóźnienie użytkownika
    List<LateFee> findAllByLoan_User_Id(Long userId);

    // Metoda do pobrania opłat za spóźnienie powyżej określonej kwoty
    List<LateFee> findAllByAmountGreaterThan(double amount);

    // Metoda do pobrania opłat za spóźnienie z określonej daty
    List<LateFee> findAllByDate(LocalDate date);
}
