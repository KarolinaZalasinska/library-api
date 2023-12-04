package com.example.libraryapi.zrobione;

import lombok.Data;
import lombok.Getter;

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

    public static RegisterResponse failure(String message) {
        return new RegisterResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
