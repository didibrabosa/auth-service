package com.example.auth_service.services;

import com.example.auth_service.dtos.LoginRequest;
import com.example.auth_service.dtos.LoginResponse;
import com.example.auth_service.dtos.RegisterRequest;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import com.example.auth_service.clients.UserServiceClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    // dependencias da classe, usando o 'final', pois não podem ser alteradas.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;

    // injeção de dependencias usando construtor.
    public AuthService(UserRepository userRepository,
    PasswordEncoder passwordEncoder, JwtService jwtService,
    UserServiceClient userServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        // metodo responsavel por registrar um novo user.
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); //criptografa a senha antes de salvar no banco.

        // apos ser criado ele salva no banco e retorna o user criado.
        userRepository.save(user);

        // chama o user-service para salvar o novo user registrado.
        userServiceClient.createUserProfile(
            new RegisterRequest(user.getId(), request.email())
        );

        return user;
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