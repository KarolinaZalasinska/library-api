package com.example.libraryapi.zrobione;

import com.example.libraryapi.model.Role;
import com.example.libraryapi.users.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // UserService zapewnia funkcje rejestracji użytkowników oraz implementuje interfejs wymagany przez Spring Security
    // do zarządzania procesem uwierzytelniania.
    @Transactional
    public RegisterResponse register(String username, String password, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            return RegisterResponse.failure("Account already exists.");
        }

        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(roleName);
        if (userRole == null) {
            return RegisterResponse.failure("Role does not exist.");
        }
        entity.addRole(userRole);

        userRepository.save(entity);
        return RegisterResponse.success();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
