package com.example.auth_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.clients.UserServiceClient;
import com.example.auth_service.dtos.LoginRequest;
import com.example.auth_service.dtos.LoginResponse;
import com.example.auth_service.dtos.RegisterRequest;
import com.example.auth_service.dtos.UserServiceRequest;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;

@Service
public class AuthService {
    // dependencias da classe, usando o 'final', pois não podem ser alteradas.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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
        logger.info("Registration attempt for email: {}", request.email());
        
        if (userRepository.findByEmail(request.email()).isPresent()) {
            logger.warn("Email already registered: {}", request.email());
            throw new RuntimeException("Email already exists");
        }

        // metodo responsavel por registrar um novo user.
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); //criptografa a senha antes de salvar no banco.

        logger.debug("Creating user for: {}", request.email());
        // apos ser criado ele salva no banco e retorna o user criado.
        User savedUser = userRepository.save(user);

        // chama o user-service para salvar o novo user registrado.
        try {
            logger.info("Calling user-service to create user profile ID: {}", savedUser.getId());
            userServiceClient.createUserProfile(
                new UserServiceRequest(savedUser.getId(), savedUser.getEmail())
            );
        } catch (Exception ex) {
            logger.error("Failed to create profile in user-service for user {}: {}", savedUser.getId(), ex.getMessage());
            throw ex;
        }

        logger.info("User successfully registered: ID {}", savedUser.getId());
        return savedUser;
    }

    public LoginResponse login(LoginRequest request) {
        // ao fazer o request de login ele busca no banco pra ver se o user ja existe.
        // se nao existir ele lança uma exception.
        logger.info("Login attempt for: {}", request.email());
        
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> {
                logger.warn("User not found: {}", request.email());
                return new RuntimeException("User not found");
            });

        // um if pra ver se a senha bate com a do user no banco.
        // se nao bate ele joga uma execption.
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("Wrong password for user: {}", request.email());
            throw new RuntimeException("Wrong password");
        }

        // gera o token jwt.
        String token = jwtService.generateToken(user.getEmail());
        logger.info("Successful login for {}", request.email());

        return new LoginResponse(token);
    }
}