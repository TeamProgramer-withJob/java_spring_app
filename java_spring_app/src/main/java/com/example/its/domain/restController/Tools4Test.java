package com.example.its.domain.restController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Tools4Test {
	private final PasswordEncoder pwdEncoder;

	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/encode")
	public String encodePwd() {
		return "@@" + pwdEncoder.encode("password") + "@@";
	}
}
