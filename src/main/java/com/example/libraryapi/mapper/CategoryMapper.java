package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.CategoryDto;
import com.example.libraryapi.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
}
