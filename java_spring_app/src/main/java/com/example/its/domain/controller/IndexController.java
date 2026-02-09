package com.example.its.domain.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.its.domain.aop.LogExecuteInfo;
import com.example.its.domain.issue.service.User2Service;
import com.example.its.domain.model.UserSignupForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final User2Service userService;
	
    @GetMapping("/test")
    public String test(Model model) {
        // test2.htmlにリダイレクトされるので、ここでは何もする必要はありません
    	String htmlContent = "<h1>test</h1>";
        model.addAttribute("htmlContent", htmlContent);
        return "test";
    }
    
    @GetMapping
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    //@RequestParam(value="error", defaultValue = "false") boolean hasError, Model model
    @LogExecuteInfo(unit = "milliseconds")
    public String showLoginForm(@RequestAttribute(name = "message", required = false) String message, Model model) {
    	model.addAttribute("error", message);
    	System.out.println("error: "+message);
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute UserSignupForm form) {
    	
    	return "signup";
    }
    @PostMapping("/signup")
    public String userSignup(@Validated UserSignupForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showSignupForm(form);
    	}
    	
    	userService.save(form);
    	return "redirect:/login";
    }
    
    @GetMapping("/access/denied")
    public String showAccessDeniedPage() {

        return "access-denied";
    }
}
