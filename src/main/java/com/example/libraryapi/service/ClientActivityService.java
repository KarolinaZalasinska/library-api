package com.example.libraryapi.service;

import com.example.libraryapi.dto.ClientActivityDto;
import com.example.libraryapi.model.ActionType;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.ClientActivity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.libraryapi.repository.ClientActivityRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientActivityService {
    private final ClientActivityRepository repository;
    private final ModelMapper modelMapper;

    public List<ClientActivityDto> getUserBorrowHistory(final Long id) {
        List<ClientActivity> userActivities = Optional.ofNullable(repository.findByUserIdAndActionType(id, ActionType.BORROW))
                .orElse(Collections.emptyList());

        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, ClientActivityDto.class))
                .collect(Collectors.toList());
    }


    public List<ClientActivityDto> getUserReturnHistory(final Long id) {
        List<ClientActivity> userActivities = Optional.ofNullable(repository.findByUserIdAndActionType(id, ActionType.RETURN))
                .orElse(Collections.emptyList());

        return userActivities.stream()
                .map(userActivity -> modelMapper.map(userActivity, ClientActivityDto.class))
                .collect(Collectors.toList());
    }
}
