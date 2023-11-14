package com.example.libraryapi.service;

import com.example.libraryapi.dto.UserActivityDto;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.UserActivityMapper;
import com.example.libraryapi.model.UserActivity;
import org.springframework.stereotype.Service;
import com.example.libraryapi.repository.UserActivityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    public final UserActivityRepository repository;
    public final UserActivityMapper mapper;

    public List<UserActivityDto> getUserBorrowHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionBorrow(id, "Borrow");
        return mapper.toDTOList(userActivities);
    }

    public List<UserActivityDto> getUserReturnHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionReturn(id, "Return");
        return mapper.toDTOList(userActivities);
    }
}
