package com.example.libraryapi.service;

import com.example.libraryapi.dto.UserDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
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
    public UserDto createUser(@Valid UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User saveUser = repository.save(user);
        return modelMapper.map(saveUser, UserDto.class);
    }

    public UserDto getUserById(final Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
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
    public UserDto updateUser(final Long id, UserDto userDto) {
        return repository.findById(id)
                .map(user -> {
                    modelMapper.map(userDto, user);
                    User updateUser = repository.save(user);
                    return modelMapper.map(updateUser, UserDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
    }

    @Transactional
    public void deleteUser(final Long id) {
        repository.deleteById(id);
    }

}
