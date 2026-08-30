package com.example.its.domain.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.its.config.ItsUserDetails;
import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.model.UserEditForm;
import com.example.its.domain.service.UserService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/plans")
@RequiredArgsConstructor
public class AdminPlanController {
	private final UserService userService;
	
    @GetMapping
    public String listUsers(Pageable pageable, Model model) {
    	Page<EntityUser> pageObj = userService.findAll(pageable);
    	model.addAttribute("page", pageObj);
    	
    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = Math.min(pageObj.getTotalPages(), idxStart + pageable.getPageSize());

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "admin/user-list";
    }
    @GetMapping("/{id}")
    public String showUserProfile(@AuthenticationPrincipal ItsUserDetails user, @RequestParam(name="page", defaultValue="0") int page, Model model, RedirectAttributes attr) {
    	EntityUser entity = null;

    	if (user == null) {
    		attr.addFlashAttribute("message", "先にログインしてください。");
        	return "redirect:/login?error";
    	} else {
    		model.addAttribute("pageNo", page);
    		String email = user.getUsername();

    		entity = userService.findUser(email);
    		if (!user.isAdmin() && !user.getUsername().equals(entity.getEmail())) {
    			// not allowed
    		}
    		
    		entity.setPlanId(2);
    		model.addAttribute("user", entity);
    		model.addAttribute("planName", "プランB");
    	}

    	return "admin/user-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, @ModelAttribute("data") UserEditForm form, Model model) {
    	if (form.getEmail() == null || form.getEmail().length() == 0) {
        	EntityUser user = userService.findUserById(id);
    		if (user.getId() > 0) {
    			model.addAttribute("data", new UserEditForm(user));
    		} else {
    			// specified user not found.
    		}
    	}
    	
    	return "admin/user-edit";
    }
    @PutMapping("/edit")
    public String updateUser(@Validated @ModelAttribute("data") UserEditForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showEditUserForm(form.getId(), form, model);
    	}
    	
    	return "admin/user-detail";
    }
    
///*    @PostMapping("/logout")
//    public String userLogout() {
//    	return "redirect:/login?logout";
//    }*/
//    
//    @GetMapping("/access/denied")
//    public String showAccessDeniedPage() {
//
//        return "access-denied";
//    }
}
