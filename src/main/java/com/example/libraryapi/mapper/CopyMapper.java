package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.model.Copy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CopyMapper extends EntityMapper<CopyDto, Copy> {
}
