package com.example.its.domain.issue.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.issue.repository.User2Repository;
import com.example.its.domain.model.UserSignupForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class User2Service {
	private final User2Repository userService;
	private final PasswordEncoder pwdEncoder;
	
	public void save(UserSignupForm user) {
		EntityUser entity = new EntityUser(user.getUsername(), pwdEncoder.encode(user.getPassword()));
		userService.save(entity);
	}
}
