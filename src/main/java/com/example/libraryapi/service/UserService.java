package com.example.libraryapi.service;

import com.example.libraryapi.dto.UserDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    @Transactional
    public UserDto createUser(@Valid final UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User saveUser = repository.save(user);
        return modelMapper.map(saveUser, UserDto.class);
    }

    public UserDto getUserById(final Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The user with the specified ID could not be found.", id));
    }

    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public UserDto updateUser(@Valid final Long id, UserDto userDto) {
        return repository.findById(id)
                .map(user -> {
                    modelMapper.map(userDto, user);
                    User updateUser = repository.save(user);
                    return modelMapper.map(updateUser, UserDto.class);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update data for user with the provided ID.", id));
    }

    @Transactional
    public void deleteUser(final Long id) {
        repository.deleteById(id);
    }

}
