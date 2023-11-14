package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.PublisherDto;
import com.example.libraryapi.model.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper extends EntityMapper<PublisherDto, Publisher> {
}
