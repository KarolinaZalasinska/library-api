package com.example.libraryapi.zrobione;

import com.example.libraryapi.users.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    // UserService zapewnia funkcje rejestracji użytkowników oraz implementuje interfejs wymagany przez Spring Security
    // do zarządzania procesem uwierzytelniania.
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void updateUser(String oldUsername, String newUsername, String newPassword, Set<String> newRoleNames) throws RoleNotFoundException {
        User user = userRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + oldUsername));

        user.setUsername(newUsername);

        user.setPassword(passwordEncoder.encode(newPassword));

        Set<Role> newRoles = new HashSet<>();
        for (String newRoleName : newRoleNames) {
            Role newRole = roleService.findRoleByName(newRoleName);
            if (newRole == null) {
                throw new RoleNotFoundException("Role not found: " + newRoleName);
            }
            newRoles.add(newRole);
        }
        user.setRoles(newRoles);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        userRepository.delete(user);
    }


    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
