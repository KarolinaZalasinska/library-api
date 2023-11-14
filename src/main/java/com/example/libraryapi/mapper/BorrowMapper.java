package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.BorrowDto;
import com.example.libraryapi.model.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper extends EntityMapper<BorrowDto, Borrow> {
}
