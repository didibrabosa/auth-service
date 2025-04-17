package com.example.auth_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// simplesmente o 'record é uma classe de dados imutáveis
public record RegisterRequest (
    @NotBlank(message = "Email is required")
    @Email(message = "Email shold be valid")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must contain at most 6 characters")
    String password
) {}
