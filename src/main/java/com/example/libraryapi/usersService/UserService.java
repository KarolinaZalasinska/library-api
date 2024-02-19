package com.example.libraryapi.usersService;

import com.example.libraryapi.exceptions.IncorrectPasswordException;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.users.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private PasswordResetService passwordResetService;
    private final ModelMapper mapper;

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    // Metody związane z użytkownikami

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> mapper.map(user, UserDto.class));
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    // Metody związane z zarządzaniem użytkownikami

    @Transactional
    public UserDto updateUserField(String username, @Valid UserDto userDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username " + username + " was not found."));

        if (userDto.newUsername() != null) {
            user.setUsername(userDto.newUsername());
        }

        // e-mail

        if (userDto.enabled()) {
            user.setEnabled(true);
        }

        User updatedUser = userRepository.save(user);
        return mapper.map(updatedUser, UserDto.class);
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

        // Dodatkowa weryfikacja poprzedniego hasła
        if (!isPreviousPasswordCorrect(user, oldPassword)) {
            throw new IncorrectPasswordException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isPreviousPasswordCorrect(User user, String oldPassword) {
        // Tutaj możesz zaimplementować dodatkową logikę weryfikacji poprzedniego hasła,
        // na przykład, jeśli chcesz przechowywać historię haseł użytkownika.
        // W tej implementacji zakładam, że użytkownik musi podać dokładnie poprzednie hasło.
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void initiatePasswordReset(String username) throws javax.mail.MessagingException {
        // Wyszukanie użytkownika w bazie danych na podstawie nazwy użytkownika
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Generowanie kodu weryfikacyjnego
        String verificationCode = passwordResetService.generateVerificationCode();

        // Wysłanie kodu weryfikacyjnego na adres e-mail użytkownika
       // passwordResetService.sendVerificationCode(user.getEmail(), verificationCode, user);
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
    użytkownika, takie jak hasło, role, czy konto jest aktywne, itd
    */