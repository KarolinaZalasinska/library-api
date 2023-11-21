package com.example.libraryapi.repository;

import com.example.libraryapi.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LibraryRepository extends JpaRepository<Library, Long> {
}
