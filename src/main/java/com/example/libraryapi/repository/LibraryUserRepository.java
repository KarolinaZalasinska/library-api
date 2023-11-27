package com.example.libraryapi.repository;

import com.example.libraryapi.model.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryUserRepository extends JpaRepository<LibraryUser,Long> {
    LibraryUser findByUsername(String username);
}
