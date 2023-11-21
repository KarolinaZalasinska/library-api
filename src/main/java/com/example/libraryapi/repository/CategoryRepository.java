package com.example.libraryapi.repository;

import com.example.libraryapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
