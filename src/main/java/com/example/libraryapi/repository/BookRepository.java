package com.example.libraryapi.repository;

import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublisher(Publisher publisher);
}
