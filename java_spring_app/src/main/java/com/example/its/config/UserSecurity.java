package com.example.its.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
	public boolean isSelf(Authentication auth, int userId) {
		if (auth != null && userId > 0 && auth.getPrincipal() instanceof ItsUserDetails user) {
			return user.getId() == userId;
		}
		return false;
	}
}
