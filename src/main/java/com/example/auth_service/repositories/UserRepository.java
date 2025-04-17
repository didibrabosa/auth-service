package com.example.auth_service.repositories;

import com.example.auth_service.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
