package com.example.libraryapi.zrobione;

import com.example.libraryapi.exceptions.IncorrectPasswordException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserEntityRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userRepository, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void updateUser(String oldUsername, String newUsername, String newPassword, Set<String> newRoleNames) throws RoleNotFoundException {
        User user = userRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + oldUsername));

        user.setUsername(newUsername);
        user.setPassword(passwordEncoder.encode(newPassword));

        Set<Role> newRoles = newRoleNames.stream()
                .map(roleService::findRoleByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        user.setRoles(newRoles);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        userRepository.delete(user);
    }

    @Transactional
    public void deleteAccount(String username, String password) throws IncorrectPasswordException, UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Weryfikacja hasła
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        userRepository.delete(user);
    }


    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws UsernameNotFoundException, IncorrectPasswordException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}
