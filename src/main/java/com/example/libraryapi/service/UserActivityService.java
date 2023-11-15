package com.example.libraryapi.service;

import com.example.libraryapi.dto.UserActivityDto;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.UserActivity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.libraryapi.repository.UserActivityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository repository;
    private final ModelMapper modelMapper;

    public List<UserActivityDto> getUserBorrowHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionBorrow(id, "Borrow");
        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, UserActivityDto.class))
                .collect(Collectors.toList());
    }

    public List<UserActivityDto> getUserReturnHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionReturn(id, "Return");
        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, UserActivityDto.class))
                .collect(Collectors.toList());
    }
}
