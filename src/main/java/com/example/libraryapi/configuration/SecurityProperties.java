package com.example.libraryapi.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "com/example/libraryapi/security")
public class SecurityProperties {
    private int maxLoginAttempts;

}
