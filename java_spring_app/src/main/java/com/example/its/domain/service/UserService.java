package com.example.its.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.model.UserSignupForm;
import com.example.its.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void save(UserSignupForm user) {
		String encryptedPwd = passwordEncoder.encode(user.getPassword());
		userRepository.save(user.getName(), encryptedPwd, user.getEmail(), "USER");
	}
	
	public int getUserId(String email) {
		return userRepository.findByEmail(email).orElse(new EntityUser()).getId();
	}
	public boolean isEmailExist(String email) {
		return userRepository.checkEmailUnique(email) != 0;
	}
}
