package com.example.auth_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// classe que leva a configuração de segurança web personalizada.
public class SecurityConfig {
    // implementação que usa o algoritmo bycrypt.
    // responsável por criptografar as passwords(register) e verificar elas(login).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // definindo as regras de acesso dos endpoints.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desabilitando a proteção csrf.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("auth/**").permitAll() // acessivel sem auth.
                .requestMatchers("api/health").permitAll() 
                .anyRequest().authenticated() // todas os outros endpoints necessitam do auth.
            );
        
        return http.build();
    }
}
