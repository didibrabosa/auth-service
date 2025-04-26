package com.example.auth_service.clients;

import com.example.auth_service.dtos.UserServiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {
    @PostMapping("/users")
    void createUserProfile(@RequestBody UserServiceRequest request);
}
