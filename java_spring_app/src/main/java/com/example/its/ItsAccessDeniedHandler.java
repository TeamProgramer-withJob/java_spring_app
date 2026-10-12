package com.example.its;

import java.io.IOException;
import java.sql.Date;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ItsAccessDeniedHandler implements AccessDeniedHandler {

	@Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

		System.out.println("tracking redirect: "+new Date(System.currentTimeMillis()));
        response.sendRedirect("/error/403");
	}
}
