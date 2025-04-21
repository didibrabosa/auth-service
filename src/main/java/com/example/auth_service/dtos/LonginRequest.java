package com.example.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;

public record LonginRequest(
    @NotBlank(message = "Email is required")
    String email,
    
    @NotBlank(message = "Password is required")
    String password
) {}
