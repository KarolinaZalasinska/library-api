package com.example.libraryapi.zrobione;

import com.example.libraryapi.exceptions.RoleNotFoundException;
import com.example.libraryapi.exceptions.reviews.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Ta klasa odpowiada za logikę rejestracji użytkowników. Przyjmuje informacje od użytkownika, sprawdza, czy użytkownik już istnieje,
// a następnie tworzy nowego użytkownika i zapisuje go w bazie danych. Jest to odpowiedzialność związana z procesem rejestracji.
@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(String username, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleService.findRoleByUserRole(role);
        user.addRole(userRole);

        userRepository.save(user);
        return RegisterResponse.success();
    }

}


