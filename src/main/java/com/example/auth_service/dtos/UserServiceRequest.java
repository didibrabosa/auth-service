package com.example.auth_service.dtos;

// dto necessario para ter compatibilidade com o serviço de user.
public record UserServiceRequest(
    String userId,
    String email
) {}
