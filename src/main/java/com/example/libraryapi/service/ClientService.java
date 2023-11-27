package com.example.libraryapi.service;

import com.example.libraryapi.dto.ClientDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Client;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.ClientRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClientDto createUser(@Valid ClientDto userDto) {
        Client user = modelMapper.map(userDto, Client.class);
        Client saveUser = userRepository.save(user);
        return modelMapper.map(saveUser, ClientDto.class);
    }

    public ClientDto getUserById(final Long id) {
        Optional<Client> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, ClientDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
    }

    public List<ClientDto> getAllUsers() {
        List<Client> users = userRepository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(user -> modelMapper.map(user, ClientDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ClientDto updateUser(final Long id, ClientDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    modelMapper.map(userDto, user);
                    Client updateUser = userRepository.save(user);
                    return modelMapper.map(updateUser, ClientDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
    }

    @Transactional
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

}
