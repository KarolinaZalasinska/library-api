package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.AuthorDto;
import com.example.libraryapi.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDto, Author> {
    Author toEntity(AuthorDto authorDto, @MappingTarget Author author);

    AuthorDto toDto(Author author);
}