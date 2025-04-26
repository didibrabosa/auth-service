package com.example.auth_service.dtos;

public record UserServiceRequest(
    String userId,
    String email
) {}
