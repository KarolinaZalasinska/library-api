package com.example.libraryapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
