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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing clients.
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new client based on the provided ClientDto.
     *
     * @param clientDto The ClientDto containing information for the new client.
     * @return The created ClientDto.
     */
    @Transactional
    public ClientDto createClient(@Valid ClientDto clientDto) {
        Client newClient = clientRepository.save(modelMapper.map(clientDto, Client.class));
        return modelMapper.map(newClient, ClientDto.class);
    }

    /**
     * Retrieves a client by its identifier.
     *
     * @param id The identifier of the client to be retrieved.
     * @return The ClientDto associated with the given id.
     * @throws ObjectNotFoundException if the client is not found.
     */
    public ClientDto getClientById(final Long id) {
        Client client = getClientOrThrow(id);
        return modelMapper.map(client, ClientDto.class);
    }

    /**
     * Retrieves all clients.
     *
     * @return A list of ClientDto representing all clients.
     */
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing client based on the provided fields.
     *
     * @param id             The identifier for the client to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @throws ObjectNotFoundException  if the client is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public void updateClient(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Client client = getClientOrThrow(id);

        Map<String, BiConsumer<Client, String>> fieldSetters = Map.of(
                "firstName", (cli, val) -> {
                    if (isValidName(val)) {
                        cli.setFirstName(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for firstName: " + val);
                    }
                },
                "lastName", (cli, val) -> {
                    if (isValidName(val)) {
                        cli.setLastName(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for lastName: " + val);
                    }
                },
                "email", (cli, val) -> {
                    if (isValidEmail(val)) {
                        cli.setEmail(val);
                    } else {
                        throw new IllegalArgumentException("Invalid value for email address: " + val);
                    }
                }
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (cl, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(client, value);
        }
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Deletes a client by its identifier.
     *
     * @param id The identifier of the client to be deleted.
     */
    @Transactional
    public void deleteClient(final Long id) {
        clientRepository.deleteById(id);
    }

    Client getClientOrThrow(final Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException("Client with id " + clientId + " was not found."));
    }
}
