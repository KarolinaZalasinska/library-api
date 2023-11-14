package com.example.libraryapi.repository;

import com.example.libraryapi.model.Book;
import com.example.libraryapi.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublisher(Publisher publisher);
}
