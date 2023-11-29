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
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClientDto createClient(@Valid ClientDto userDto) {
        Client user = modelMapper.map(userDto, Client.class);
        Client saveUser = clientRepository.save(user);
        return modelMapper.map(saveUser, ClientDto.class);
    }

    public ClientDto getClientById(final Long id) {
        Optional<Client> optionalUser = clientRepository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, ClientDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
    }

    public List<ClientDto> getAllClients() {
        List<Client> users = clientRepository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(user -> modelMapper.map(user, ClientDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ClientDto updateClient(final Long id, ClientDto userDto) {
        return clientRepository.findById(id)
                .map(user -> {
                    modelMapper.map(userDto, user);
                    Client updateUser = clientRepository.save(user);
                    return modelMapper.map(updateUser, ClientDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " was not found."));
    }

    @Transactional
    public void deleteClient(final Long id) {
        clientRepository.deleteById(id);
    }

}
