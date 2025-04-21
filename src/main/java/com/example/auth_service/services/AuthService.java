package com.example.auth_service.services;

import com.example.auth_service.dtos.LoginRequest;
import com.example.auth_service.dtos.LoginResponse;
import com.example.auth_service.dtos.RegisterRequest;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    // dependencias da classe, usando o 'final', pois não podem ser alteradas.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // injeção de dependencias usando construtor.
    public AuthService(UserRepository userRepository,
    PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {
        // metodo responsavel por registrar um novo user.
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); //criptografa a senha antes de salvar no banco.

        // apos ser criado ele salva no banco e retorna o user criado.
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        // ao fazer o request de login ele busca no banco pra ver se o user ja existe.
        // se nao existir ele lança uma exception.
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // um if pra ver se a senha bate com a do user no banco.
        // se nao bate ele joga uma execption.
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // gera o token jwt.
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}