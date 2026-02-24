package com.example.its.domain.auth;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    private String displayName;
    private String email;
    private String password;
    private String bio;
    private String role;
    private LocalDateTime createdAt;
}
