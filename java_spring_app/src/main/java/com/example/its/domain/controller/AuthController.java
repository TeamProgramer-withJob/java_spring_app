package com.example.its.domain.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.its.config.ItsUserDetails;
import com.example.its.domain.annot.OnCreate;
import com.example.its.domain.annot.OnEdit;
import com.example.its.domain.aop.LogExecuteInfo;
import com.example.its.domain.model.UserSignupForm;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
//	private final MessageSource messageSource;
	
    @GetMapping
    public String index() {
        return "redirect:/news";
    }
    @GetMapping("/login")
    //@RequestParam(value="error", defaultValue = "false") boolean hasError, Model model
    @LogExecuteInfo(unit = "milliseconds")
    public String showLoginForm(Model model) {
    	model.addAttribute("activeMenu", "login");
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute("data") UserSignupForm form) {
    	return "signup";
    }
    @PostMapping("/signup")
    public String userSignup(@Validated(OnCreate.class) @ModelAttribute("data") UserSignupForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showSignupForm(form);
    	}
    	
    	userService.save(form);
    	return "redirect:/login";
    }
    
    @GetMapping("/change-password")
    public String showForceUser2ChangePwd(Authentication auth, @ModelAttribute("data") UserSignupForm form, Model model) {
    	ItsUserDetails userDetails = (ItsUserDetails)auth.getPrincipal();
    	form.setEmail(userDetails.getUsername());

    	return "change-password";
    }
    
    @PostMapping("/change-password")
    public String forceUser2ChangePwd(Authentication auth, @Validated(OnEdit.class) @ModelAttribute("data") UserSignupForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showForceUser2ChangePwd(auth, form, model);
    	}
    	
    	ItsUserDetails userDetails = (ItsUserDetails)auth.getPrincipal();
    	userService.updatePwd(userDetails.getUsername(), form.getPassword());
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
