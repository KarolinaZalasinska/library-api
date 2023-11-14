package com.example.libraryapi.repository;

import com.example.libraryapi.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    // Metoda zwracająca historię wypożyczeń dla konkretnego użytkownika
    List<UserActivity> findByUserIdAndActionBorrow(Long id, String action);

    // Metoda zwracająca historię zwrotów dla konkretnego użytkownika
    List<UserActivity> findByUserIdAndActionReturn(Long id, String action);
}
