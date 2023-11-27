//package security;
//
//import db.UserEntityRepository;
//import domain.UserEntity;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Data
//public class UserEntityDetailsService implements UserDetailsService { // klasa odpowiedzialna za dostarczanie informacji o użytkowniku.
//    private final UserEntityRepository userEntityRepository;
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity user = userEntityRepository.findByUsernameIgnoreCase(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        return new UserEntityDetails(user);
//    }
//}
