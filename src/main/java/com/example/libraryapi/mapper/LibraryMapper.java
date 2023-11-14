package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.LibraryDto;
import com.example.libraryapi.model.Library;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryMapper extends EntityMapper<LibraryDto, Library> {
}
