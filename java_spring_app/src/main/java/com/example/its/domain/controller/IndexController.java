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
	
	private final MessageSource messageSource;
	
	
    @GetMapping("/test")
    public String test(Model model) {
        // test2.htmlにリダイレクトされるので、ここでは何もする必要はありません
    	String htmlContent = "<h1>test</h1>";
    	List<String> myList = Arrays.asList("鍾志華", "Tom", "Bob");
        model.addAttribute("mylist", myList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:sss");
        model.addAttribute("today", sdf.format(LocalDateTime.now()));
        return "test";
    }
    
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
    
/*    @PostMapping("/logout")
    public String userLogout() {
    	return "redirect:/login?logout";
    }*/
    
    @GetMapping("/access/denied")
    public String showAccessDeniedPage() {

        return "access-denied";
    }
}
