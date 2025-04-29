package com.example.auth_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
// classe que leva a configuração de segurança web personalizada.
public class SecurityConfig {
    // implementação que usa o algoritmo bycrypt.
    // responsável por criptografar as passwords(register) e verificar elas(login).
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    // definindo as regras de acesso dos endpoints.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain...");
        http
            .csrf(csrf -> csrf.disable()) // desabilitando a proteção csrf.
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("auth/**").permitAll() // acessivel sem auth.
                    .requestMatchers("api/health").permitAll() 
                    .anyRequest().authenticated(); // todas os outros endpoints necessitam do auth.

                logger.info("Public endpoints: /auth/**, /api/health");
                logger.info("All other endpoints require authentication");
            });
        
        logger.info("SecurityFilterChain configured successfully");    
        return http.build();
    }
}