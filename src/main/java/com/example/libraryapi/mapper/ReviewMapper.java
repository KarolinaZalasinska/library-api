package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.ReviewDto;
import com.example.libraryapi.model.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDto, Review> {
}
