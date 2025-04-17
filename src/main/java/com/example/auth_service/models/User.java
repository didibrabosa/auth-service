package com.example.auth_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // documentando o nome da coleção no MongoDB
public class User {
    @Id
    private String id;

    private String email;
    private String password;

    //por algum motivo no mongo é obrigatorio um construtor vazio (??????)
    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // omg famosos getters e setters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
