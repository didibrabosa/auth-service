package com.example.auth_service.dtos;

// aqui vai retornar so o token jwt apos o request de login.
public record LoginResponse(
    String token
) {}
