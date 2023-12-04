//package com.example.libraryapi.zrobione;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService {
//    private final UserService userService;
//
//    public CustomUserDetailsService(UserService userService) {
//        this.userService = userService;
//    }
//
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userService.loadUserByUsername(username);
//    }
//}
//
