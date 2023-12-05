package com.example.libraryapi.zrobione;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Ta klasa odpowiada za logikę rejestracji użytkowników. Przyjmuje informacje od użytkownika, sprawdza, czy użytkownik już istnieje,
// a następnie tworzy nowego użytkownika i zapisuje go w bazie danych. Jest to odpowiedzialność związana z procesem rejestracji.
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(String username, String password, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            return RegisterResponse.failure("Account already exists.");
        }

        User entity = new User();
        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));

        Role userRole = roleService.findRoleByName(roleName);
        if (userRole == null) {
            return RegisterResponse.failure("Role does not exist.");
        }
        entity.addRole(userRole);

        userRepository.save(entity);
        return RegisterResponse.success();
    }
}
