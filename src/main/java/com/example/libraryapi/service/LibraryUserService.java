package com.example.libraryapi.service;

import com.example.libraryapi.model.LibraryUser;
import com.example.libraryapi.repository.LibraryUserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class LibraryUserService {
    private LibraryUserRepository libraryUserRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public void saveUser(LibraryUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        libraryUserRepository.save(user);
    }
}
