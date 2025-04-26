package com.example.auth_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {
    @PostMapping("/users")
    void createUserProfile(
        @RequestParam("userId") String userId,
        @RequestParam("email") String email
    );
}
