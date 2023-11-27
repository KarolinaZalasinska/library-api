package com.example.libraryapi.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class BaseSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public InitializingBean initializingBean(UserDetailsManager userDetailsManager) {
        return () -> {
            System.out.println("Hello from init bean");

            UserDetails user = User
                    .builder()
                    .passwordEncoder(password ->
                            PasswordEncoderFactories
                                    .createDelegatingPasswordEncoder()
                                    .encode(password))
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();

            userDetailsManager.createUser(user);

            UserDetails admin = User
                    .builder()
                    .passwordEncoder(password ->
                            PasswordEncoderFactories
                                    .createDelegatingPasswordEncoder()
                                    .encode(password))
                    .username("admin")
                    .password("password")
                    .roles("ADMIN")
                    .build();

            userDetailsManager.createUser(admin);
        };
    }
}

