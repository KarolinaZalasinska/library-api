package service;

import dto.UserDto;
import exception.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import mapper.UserMapper;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository repository;
    public final UserMapper mapper;

    @Transactional
    public UserDto createUser(final UserDto userDto) {
        User user = mapper.toEntity(userDto);
        User saveUser = repository.save(user);
        return mapper.toDto(saveUser);
    }

    public UserDto getUserById(final Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The user with the specified ID could not be found.", id));
    }

    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public UserDto updateUser(final Long id, UserDto userDto) {
        return repository.findById(id)
                .map(user -> {
                    mapper.updateEntityFromDto(userDto, user);
                    User updateUser = repository.save(user);
                    return mapper.toDto(updateUser);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("Failed to update data for user with the provided ID.", id));
    }

    @Transactional
    public void deleteUser(final Long id) {
        repository.deleteById(id);
    }
}
