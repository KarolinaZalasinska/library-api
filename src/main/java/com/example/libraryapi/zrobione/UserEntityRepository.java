package com.example.libraryapi.zrobione;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
