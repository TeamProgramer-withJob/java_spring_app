package com.example.its.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ItsSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		ItsUserDetails user = (ItsUserDetails)auth.getPrincipal();
		
		if (user.isForcePwdChange()) {
//			response.sendRedirect("/change-password");
			response.sendRedirect("/memories");
			return;
		} if (user.isAdmin()) {
			response.sendRedirect("/news");
			return;
		}
		
		response.sendRedirect("/memories");
	}
}
