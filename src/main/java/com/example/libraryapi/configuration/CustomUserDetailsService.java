package com.example.libraryapi.configuration;

import com.example.libraryapi.model.LibraryUser;
import com.example.libraryapi.repository.LibraryUserRepository;
import com.example.libraryapi.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LibraryUserRepository repository;
    private final RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LibraryUser libraryUser = repository.findByUsername(username);
        if (libraryUser == null) {
            throw new UsernameNotFoundException("Użytkownik o nazwie: " + username + " nie został znaleziony");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(libraryUser.getUsername())
                .password(libraryUser.getPassword())
                .authorities(libraryUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()))
                .accountExpired(!libraryUser.isAccountNonExpired())
                .accountLocked(!libraryUser.isAccountNonLocked())
                .credentialsExpired(!libraryUser.isCredentialsNonExpired())
                .disabled(!libraryUser.isEnabled())
                .build();
    }
}
