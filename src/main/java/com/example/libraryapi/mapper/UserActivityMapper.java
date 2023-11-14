package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.UserActivityDto;
import com.example.libraryapi.model.Borrow;
import com.example.libraryapi.model.UserActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserActivityMapper extends EntityMapper<UserActivityDto, UserActivity> {
    UserActivityDto mapBorrowToUserActivityDto(Borrow borrow);
}
