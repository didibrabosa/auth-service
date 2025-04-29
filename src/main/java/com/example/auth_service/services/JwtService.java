package com.example.auth_service.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtService {
    // gera uma secret key pra assinar os tokens jwt
    // o 'final' indica que ela não pode ser mudada após a criação(ao instanciar a classe).
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // responsavel por verificar em properties quanto tempo o token vai expirar.
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // metodo pra criar o token jwt à partir do email.
    public String generateToken(String email) {
        logger.debug("Generating JWT token for: {}", email);
        try {
         String token = Jwts.builder() // construtor do jwt.
            .setSubject(email) // usa o email com identificador.
            .setIssuedAt(new Date()) // data/hora da criação do token.
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // define quando o token vai expirar.
            .signWith(secretKey) // usa a secretkey pra assinar o token.
            .compact(); // compacta no formato jwt.
    
            logger.info("Token successfully generated for: {}", email);
            return token;

        } catch (Exception ex) {
            logger.error("Failed to generate token for {}: {}", email, ex.getMessage());
            throw new RuntimeException("Token generation failed", ex);
        }
    }
}
