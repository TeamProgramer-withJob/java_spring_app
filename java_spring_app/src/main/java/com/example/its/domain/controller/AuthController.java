package com.example.its.domain.controller;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.its.domain.aop.LogExecuteInfo;
import com.example.its.domain.model.InquiryForm;
import com.example.its.domain.model.UserSignupForm;
import com.example.its.domain.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
//	private final MessageSource messageSource;
	
    @GetMapping
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    //@RequestParam(value="error", defaultValue = "false") boolean hasError, Model model
    @LogExecuteInfo(unit = "milliseconds")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute("data") UserSignupForm form) {
    	return "signup";
    }
    @PostMapping("/signup")
    public String userSignup(@Validated @ModelAttribute("data") UserSignupForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showSignupForm(form);
    	}
    	
    	userService.save(form);
    	return "redirect:/login";
    }
    
/*    @PostMapping("/logout")
    public String userLogout() {
    	return "redirect:/login?logout";
    }*/
    
    @GetMapping("/access/denied")
    public String showAccessDeniedPage() {

        return "access-denied";
    }
}
