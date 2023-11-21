package com.example.libraryapi.service;

import com.example.libraryapi.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
private final SecurityProperties securityProperties;
    // private static final int MAX_LOGIN_ATTEMPTS = 3;
    @Value("${security.max-login-attempts}")
    private int maxLoginAttempts;
}
