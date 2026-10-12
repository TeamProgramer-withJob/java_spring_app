package com.example.its.config;

import java.sql.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
	public boolean isSelf(Authentication auth, int userId) {
		System.out.println("tracking0: "+new Date(System.currentTimeMillis()));
		if (auth != null && userId > 0 && auth.getPrincipal() instanceof ItsUserDetails user) {
			System.out.println("tracking1: "+new Date(System.currentTimeMillis()));
			return user.getId() == userId;
		}
		System.out.println("tracking3: "+new Date(System.currentTimeMillis()));
		return false;
	}
}
