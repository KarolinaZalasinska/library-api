package com.example.libraryapi.repository;

import com.example.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
}
