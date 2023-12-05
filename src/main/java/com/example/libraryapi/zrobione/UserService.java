package com.example.libraryapi.zrobione;

import com.example.libraryapi.exceptions.IncorrectPasswordException;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper mapper;


    private static final int MAX_LOGIN_ATTEMPTS = 3;

    // Metody związane z użytkownikami

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    // Metody związane z zarządzaniem użytkownikami

    @Transactional
    public UserUpdateDto updateUserField(String username, Map<String, String> fieldsToUpdate) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username " + username + " was not found."));

        fieldsToUpdate.forEach((field, value) -> {
            switch (field) {
                case "username":
                    user.setUsername(value);
                case "password":
                    if (!isValidPassword(value)) {
                        throw new ValidationException("Invalid new password");
                    }
                    user.setPassword(passwordEncoder.encode(value));
                    break;

                // Dodaj obsługę innych pól, jeśli to konieczne

                default:
                    throw new IllegalArgumentException("Invalid field specified: " + field);
            }
        });

        User updatedUser = userRepository.save(user);
        return mapper.map(updatedUser, UserUpdateDto.class);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        userRepository.delete(user);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public void deleteAccount(String username, String password) throws IncorrectPasswordException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        userRepository.delete(user);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws IncorrectPasswordException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Metody związane z obsługą logowania

    @Transactional
    public void handleLoginFailure(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (user.getLoginAttempts() < MAX_LOGIN_ATTEMPTS - 1) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
        } else {
            user.setAccountLocked(true);
            user.setLockExpirationTime(LocalDateTime.now().plusHours(24));
        }

        userRepository.save(user);
    }

    public void handleSuccessfulLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        user.setLoginAttempts(0);
        user.setAccountLocked(false);
        userRepository.save(user);
    }

    // Metoda związana z wczytywaniem szczegółów użytkownika

    @Transactional
    public UserDetails loadUserDetailsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }
}

    /*
    metoda loadUserDetailsByUsername jest odpowiedzialna za wczytywanie informacji o użytkowniku na podstawie nazwy użytkownika i tworzenie
    obiektu CustomUserDetails, który implementuje interfejs UserDetails. Ta klasa dostarcza informacje potrzebne do uwierzytelniania
    użytkownika, takie jak hasło, role, czy konto jest aktywne, itp.
    */