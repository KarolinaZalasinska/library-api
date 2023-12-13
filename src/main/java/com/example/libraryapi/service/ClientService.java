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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClientDto createClient(@Valid ClientDto clientDto) {
        Client newClient = clientRepository.save(modelMapper.map(clientDto, Client.class));
        return modelMapper.map(newClient, ClientDto.class);
    }

    public ClientDto getClientById(final Long id) {
        return clientRepository.findById(id)
                .map(client -> modelMapper.map(client, ClientDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Client with id " + id + " was not found."));
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDto updateClient(final Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client with id " + id + " was not found."));

        modelMapper.map(clientDto, client);
        Client updatedClient = clientRepository.save(client);

        return modelMapper.map(updatedClient, ClientDto.class);
    }

    @Transactional
    public void deleteClient(final Long id) {
        clientRepository.deleteById(id);
    }

}
