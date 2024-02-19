//package com.example.libraryapi.test;
//
//import com.example.libraryapi.dto.BookDto;
//import com.example.libraryapi.mapper.ModelMapperConfig;
//import com.example.libraryapi.model.Book;
//import com.example.libraryapi.model.CopyStatus;
//import com.example.libraryapi.repository.BookRepository;
//import com.example.libraryapi.service.BookService;
//import lombok.Data;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//
//import static junit.framework.TestCase.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
//public class BookServiceTest {
//    @Mock
//    private BookRepository repository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private BookService bookService;
//    private BookDto bookDto;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        bookDto = new BookDto(
//                1L,
//                "Test Book",
//                "1234567890",
//                CopyStatus.AVAILABLE,
//                new HashSet<>(Arrays.asList(1L, 2L)),
//                new ArrayList<>(Arrays.asList(1L, 2L)),
//                new ArrayList<>(Arrays.asList(1L, 2L)),
//                1L,
//                new HashSet<>(Arrays.asList(1L, 2L)),
//                new HashSet<>(Arrays.asList(1L, 2L))
//        );
//    }
//
//
//    @Test
//    public void testCreateBook() {
//        Book savedBook = new Book();
//        savedBook.setId(1L);
//        savedBook.setTitle();
//        savedBook.setIsbn(bookDto.isbn());
//
//        when(repository.save(any(Book.class))).thenReturn(savedBook);
//
//        // Wywołanie metody createBook z BookService
//        //BookDto createdBookDto = bookService.createBook(bookDto);
//        Book createdBook = bookService.createBook(bookDto);
//
//        // Sprawdzenie, czy odpowiednio zachowano się repozytorium
//        verify(repository, times(1)).save(any(Book.class));
//
//        // Sprawdzenie, czy zwrócono poprawny obiekt BookDto
//        assertEquals(savedBook.getId(), createdBookDto.id());
//        assertEquals(bookDto.title(), createdBookDto.title());
//        assertEquals(bookDto.isbn(), createdBookDto.isbn());
//    }
//
//}