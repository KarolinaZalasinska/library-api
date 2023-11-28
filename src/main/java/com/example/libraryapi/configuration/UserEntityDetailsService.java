package com.example.libraryapi.configuration;

import db.UserEntityRepository;
import domain.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.UserEntityDetails;

@Service
public class UserEntityDetailsService implements UserDetailsService { // klasa odpowiedzialna za dostarczanie informacji o użytkowniku.
    private final UserEntityRepository userEntityRepository;

    public UserEntityDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new UserEntityDetails(user);
    }
}

