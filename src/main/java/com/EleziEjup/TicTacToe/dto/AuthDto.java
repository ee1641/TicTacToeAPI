package com.EleziEjup.TicTacToe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthDto {
    @NotBlank(message = "username is required")
    @Size(min = 1, max = 20, message = "Username must be between 1 to 20 char")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 1 char long")
    private String password;
}
