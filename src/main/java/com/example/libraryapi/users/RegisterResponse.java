package com.example.libraryapi.users;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class RegisterResponse {
    private final boolean success;
    private final String message;

    public static RegisterResponse success() {

        return new RegisterResponse(true, "Registration successful");
    }

    public static RegisterResponse failure(String message) {

        return new RegisterResponse(false, message);
    }

}
