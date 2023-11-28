package com.example.libraryapi.service;

import db.UserEntityRepository;
import domain.UserEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class LibraryUserService {
    private UserEntityRepository userEntityRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public void saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntityRepository.save(user);
    }
}
