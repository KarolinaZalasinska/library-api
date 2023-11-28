package com.example.libraryapi.users;

import com.example.libraryapi.model.Role;
import com.example.libraryapi.db.RoleRepository;
import com.example.libraryapi.db.UserEntityRepository;
import com.example.libraryapi.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.security.RegisterResponse;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserEntityRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public RegisterResponse register(String username, String password) {
        if (repository.findByUsername(username).isPresent()) {
            return RegisterResponse.failure("Account already exists.");
        }

        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(encoder.encode(password));

        // Ustawienia dodatkowe dla nowego użytkownika (jeśli istnieją)
        // entity.setFirstName(firstName);
        // entity.setLastName(lastName);

        Role userRole = roleRepository.findByName("ROLE_USER");
        entity.setRoles(Set.of(userRole));

        repository.save(entity);
        return RegisterResponse.success();
    }

    public void registerUserWithRole(String username, String password, String roleName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(encoder.encode(password));

        // Ustawienia dodatkowe dla nowego użytkownika (jeśli istnieją)
        // userEntity.setFirstName(firstName);
        // userEntity.setLastName(lastName);

        Role role = new Role();

        userEntity.addRole(role);
        repository.save(userEntity);
    }
}
