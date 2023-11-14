package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDto, Book> {
    Book toEntity(BookDto bookDto, @MappingTarget Book book);
    BookDto toDto(Book book);
    Set<BookDto> toDtoSet(Set<Book> books);
}
