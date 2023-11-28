package users;

import db.UserEntityRepository;
import domain.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> libraryUserOptional = userEntityRepository.findByUsername(username);
        UserEntity libraryUser = libraryUserOptional.orElseThrow(() ->
                new UsernameNotFoundException("Użytkownik o nazwie: " + username + " nie został znaleziony"));

        return org.springframework.security.core.userdetails.User
                .withUsername(libraryUser.getUsername())
                .password(libraryUser.getPassword())
                .authorities(libraryUser.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))
                .accountExpired(!libraryUser.isAccountNonExpired())
                .accountLocked(!libraryUser.isAccountNonLocked())
                .credentialsExpired(!libraryUser.isCredentialsNonExpired())
                .disabled(!libraryUser.isEnabled())
                .build();
    }
}
