package com.example.libraryapi.zrobione;

import lombok.Getter;

@Getter
public class RegisterResponse {

    private final boolean success;
    private final String message;

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static RegisterResponse success() {
        return new RegisterResponse(true, "Registration successful");
    }

}
