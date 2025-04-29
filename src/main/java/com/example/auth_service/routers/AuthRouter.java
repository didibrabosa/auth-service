package com.example.auth_service.routers;

import com.example.auth_service.dtos.LoginRequest;
import com.example.auth_service.dtos.LoginResponse;
import com.example.auth_service.dtos.RegisterRequest;
import com.example.auth_service.models.User;
import com.example.auth_service.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthRouter {
    
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthRouter.class);

    public AuthRouter(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
        @Valid @RequestBody RegisterRequest request
    ) {
        logger.info("Register request received for email: {}", request.email());
        
        try {
            User registeredUser = authService.register(request);
            logger.info("Register completed successfully for email: {}", request.email());
            return ResponseEntity.ok(registeredUser);
            
        } catch (Exception ex) {
            logger.error("Register failed for email {}: {}", request.email(), ex.getMessage());
            throw ex;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        logger.info("Login request received for email: {}", request.email());
        
        try {
            LoginResponse response = authService.login(request);
            logger.info("Login successful for email: {}", request.email());
            return ResponseEntity.ok(response);
            
        } catch (Exception ex) {
            logger.warn("Login failed for email {}: {}", request.email(), ex.getMessage());
            throw ex;
        }
    }
}