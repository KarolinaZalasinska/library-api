package com.example.libraryapi.securityConfig;

import com.example.libraryapi.db.UserEntityRepository;
import com.example.libraryapi.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.libraryapi.users.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UserEntityDetailsService implements UserDetailsService { // klasa odpowiedzialna za dostarczanie informacji o użytkowniku.
    private final UserEntityRepository repository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }
}

