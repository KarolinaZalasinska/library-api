package repository;

import model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    // Metoda zwracająca historię wypożyczeń dla konkretnego użytkownika
    List<UserActivity> findByUserIdAndActionEquals(Long userId, String actionBorrow);

    // Metoda zwracająca historię zwrotów dla konkretnego użytkownika
    List<UserActivity> findByUserIdAndActionEquals(Long userId, String actionReturn);
}
