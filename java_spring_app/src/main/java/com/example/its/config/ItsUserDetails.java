package com.example.its.config;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.its.domain.entity.EntityUser;

public class ItsUserDetails implements UserDetails {
	private final EntityUser user;

	public ItsUserDetails(EntityUser user) {
		this.user = user;
	}

	public int getId() {
		return user.getId();
	}
	public String getRole() {
		return user.getRole();
	}
	public boolean isAdmin() {
		return user.getRole().equals("ADMIN");
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
	}

	@Override
	public @Nullable String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
