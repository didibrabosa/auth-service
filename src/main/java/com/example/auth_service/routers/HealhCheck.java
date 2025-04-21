package com.example.auth_service.routers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealhCheck {
    
    @GetMapping("/health")
    public String healthCheck() {
        return "API is up and health!";
    }
}
