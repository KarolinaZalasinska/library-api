package com.example.libraryapi.securityConfig;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UserDetailsManagerConfig {

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
        userDetailsManager.setGroupAuthoritiesByUsernameQuery("SELECT g.id, g.group_name, ga.authority FROM groups g, group_members gm, group_authorities ga WHERE gm.username = ? AND g.id = ga.group_id AND g.id = gm.group_id");
        userDetailsManager.setCreateAuthoritySql("INSERT INTO authorities (username, authority) VALUES (?, ?)");
        userDetailsManager.setCreateUserSql("INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)");

        return userDetailsManager;
    }

    @Bean
    public InitializingBean initializingBean(JdbcUserDetailsManager userDetailsManager) {
        return () -> {
            UserDetails user = User
                    .builder()
                    .passwordEncoder(password ->
                            PasswordEncoderFactories
                                    .createDelegatingPasswordEncoder()
                                    .encode(password))
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
                    .passwordEncoder(password ->
                            PasswordEncoderFactories
                                    .createDelegatingPasswordEncoder()
                                    .encode(password))
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
