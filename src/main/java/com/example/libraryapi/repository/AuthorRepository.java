package com.example.libraryapi.repository;

import com.example.libraryapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
