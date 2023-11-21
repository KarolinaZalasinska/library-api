package com.example.libraryapi.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
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
                    .roles("ADMIN")
                    .build();
            userDetailsManager.createUser(user);

        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults());

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests(authorizationMatcher ->
                        authorizationMatcher
                                .anyRequest()
                                .permitAll()
                );

        return httpSecurity.build();
    }
}

