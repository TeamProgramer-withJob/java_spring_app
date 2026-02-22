package com.example.its.domain.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

    private final Long userId;
    private final String displayName;

    public CustomUserDetails(Long userId, String username, String displayName,
                             String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.displayName = displayName;
    }
}
