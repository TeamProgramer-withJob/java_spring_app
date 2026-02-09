package com.example.its;

import java.io.IOException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class WebAuthFailedHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {
		String errorMessage;
        if (ex.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            errorMessage = "usernameNotFound";
        } else if (ex.getClass().isAssignableFrom(BadCredentialsException.class)) {
            // By default, Spring Security throws BadCredentialsException for both wrong username and wrong password.
            // More on this below.
            errorMessage = "badCredentials";
        } else if (ex.getClass().isAssignableFrom(DisabledException.class)) {
            errorMessage = "accountDisabled";
        } else {
            errorMessage = "unknownError";
        }
        request.setAttribute("message", errorMessage);
        // Redirect to the login page with an error parameter
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, ex);
	}

}
