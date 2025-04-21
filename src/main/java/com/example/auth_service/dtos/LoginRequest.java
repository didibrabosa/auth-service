package com.example.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;

//aqui um dto pra lidar com o request de login.
public record LoginRequest(
    @NotBlank(message = "Email is required")
    String email,
    
    @NotBlank(message = "Password is required")
    String password
) {}
