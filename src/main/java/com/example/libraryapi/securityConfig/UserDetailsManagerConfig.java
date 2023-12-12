package com.example.libraryapi.securityConfig;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UserDetailsManagerConfig {

    private final DataSource dataSource;

    public UserDetailsManagerConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // jdbcUserDetailsManager zajmuje się konfiguracją dostępu (zarządzaniem) do danych użytkowników przechowywanych w bazie danych
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.username, r.role FROM users u JOIN role r ON u.id = r.user_id WHERE u.username = ?");
        userDetailsManager.setCreateAuthoritySql("INSERT INTO authorities (username, role) VALUES (?, ?)");
        userDetailsManager.setCreateUserSql("INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)");

        return userDetailsManager;
    }

    // metoda jest używana do inicjalizacji użytkowników w systemie przy uruchamianiu aplikacji
    @Bean
    public InitializingBean initializingBean(JdbcUserDetailsManager userDetailsManager) {
        return () -> {
            UserDetails user = User
                    .builder()
                    .passwordEncoder(new BCryptPasswordEncoder()::encode)
                    .username("unique_user")
                    .password("password")
                    .roles("USER")
                    .authorities("ROLE_USER")
                    .build();

            if (!userDetailsManager.userExists(user.getUsername())) {
                userDetailsManager.createUser(user);
            }

            UserDetails admin = User
                    .builder()
                    .passwordEncoder(new BCryptPasswordEncoder()::encode)
                    .username("unique_admin")
                    .password("password")
                    .roles("ADMIN")
                    .authorities("ROLE_ADMIN")
                    .build();

            if (!userDetailsManager.userExists(admin.getUsername())) {
                userDetailsManager.createUser(admin);
            }
        };
    }
}
