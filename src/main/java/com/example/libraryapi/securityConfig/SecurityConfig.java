package com.example.libraryapi.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    // configure zajmuje się konfiguracją samego mechanizmu uwierzytelniania w kontekście Spring Security
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, role FROM roles WHERE username = ?")
                .passwordEncoder(passwordEncoder);
    }

    // dostawca uwierzytelnienia
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsManager userDetailsManager) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsManager);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
