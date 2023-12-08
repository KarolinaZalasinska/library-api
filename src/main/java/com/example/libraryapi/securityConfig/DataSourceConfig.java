package com.example.libraryapi.securityConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/library_api");
        config.setUsername("root");
        config.setPassword("Wojcik.1994");

        //  Konfiguruje źródło danych (DataSource) tak, aby używało algorytmu haszowania (czyli BCrypt PE).
        config.addDataSourceProperty("spring.datasource.password", "bcrypt");

        return new HikariDataSource(config);
    }
}

// Hikari - narzędzie do zarządzania pulą połączeń z bazą danych.
// HikariCP - odpowiada za efektywne zarządzanie połączeniami do bazy danych.