package com.example.its.config;

import org.springframework.security.core.AuthenticationException;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class ItsAfterAuthenticationHandler {
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		ItsUserDetails user = (ItsUserDetails)event.getAuthentication().getPrincipal();
		// do some logging
	}
	
	@EventListener
	public void onFailure(AbstractAuthenticationFailureEvent event) {
		String userName = (String)event.getAuthentication().getPrincipal();

		// do some logging
//		System.out.println(userName);
	}
}
