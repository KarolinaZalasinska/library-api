package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.UserDto;
import com.example.libraryapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
}
