package com.example.libraryapi.service;

import com.example.libraryapi.dto.UserActivityDto;
import com.example.libraryapi.model.ActionType;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.UserActivity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.libraryapi.repository.UserActivityRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository repository;
    private final ModelMapper modelMapper;

    public List<UserActivityDto> getUserBorrowHistory(final Long id) {
        List<UserActivity> userActivities = Optional.ofNullable(repository.findByUserIdAndActionType(id, ActionType.BORROW))
                .orElse(Collections.emptyList());

        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, UserActivityDto.class))
                .collect(Collectors.toList());
    }


    public List<UserActivityDto> getUserReturnHistory(final Long id) {
        List<UserActivity> userActivities = Optional.ofNullable(repository.findByUserIdAndActionType(id, ActionType.RETURN))
                .orElse(Collections.emptyList());

        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, UserActivityDto.class))
                .collect(Collectors.toList());
    }
}
