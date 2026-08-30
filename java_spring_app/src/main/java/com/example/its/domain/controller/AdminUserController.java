package com.example.its.domain.controller;


import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
import com.example.its.domain.model.PulldownIdName;
import com.example.its.domain.model.UserEditForm;
import com.example.its.domain.service.UserService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
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
    public String showUserProfile(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model, RedirectAttributes attr) {
    	EntityUser entity = null;

    	if (user == null) {
    		attr.addFlashAttribute("message", "先にログインしてください。");
        	return "redirect:/login?error";
    	} else {
    		model.addAttribute("pageNo", page);

    		entity = userService.findUserById(id);
    		if (entity.getId() <= 0) {
				model.addAttribute("error", "ユーザーは見つかりません。");
    		} else if (!user.isAdmin() && !user.getUsername().equals(entity.getEmail())) {
				model.addAttribute("error", "アクセス権限はありません。");
    		}

    		model.addAttribute("data", new UserEditForm(entity));
    	}

    	return "admin/user-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, 
    		                       @ModelAttribute("data") UserEditForm form, Model model) {
		model.addAttribute("allRoles", List.of(new PulldownIdName("ADMIN", "管理者"), new PulldownIdName("USER", "ユーザー")));
		model.addAttribute("pageNo", page);
    	if (model.getAttribute("error") == null && (form.getEmail() == null || form.getEmail().length() == 0)) {
        	EntityUser entity = userService.findUserById(id);
			model.addAttribute("data", new UserEditForm(entity));

			if (entity.getId() <= 0) {
				model.addAttribute("error", "ユーザーは見つかりません。");
    		} else if (!user.isAdmin() && !user.getUsername().equals(entity.getEmail())) {
				model.addAttribute("error", "アクセス権限はありません。");
    		}
    	}
    	
    	return "admin/user-edit";
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #id)")
    public String updateUser(@AuthenticationPrincipal ItsUserDetails user, @P("id") @PathVariable(name="id") int id, @RequestParam(name="page", defaultValue="0") int page,
    		                 @Validated @ModelAttribute("data") UserEditForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showEditUserForm(user, form.getId(), page, form, model);
    	}
    	userService.update(form, user.isAdmin());

    	return "redirect:/admin/users/" + form.getId() + "?page=" + page;
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model) {
    	EntityUser entity = userService.findUserById(id);
    	UserEditForm form = new UserEditForm(entity);
    	model.addAttribute("data", form);
    	
    	if (entity.getId() <= 0) {
			model.addAttribute("error", "ユーザーは見つかりません。");
    		return showEditUserForm(user, id, page, form, model);
    	} else if (!user.isAdmin()) {
			model.addAttribute("error", "アクセス権限はありません。");
    		return showEditUserForm(user, id, page, form, model);
    	}
    	try {
        	userService.delete(id);
    	} catch (Exception ex) {
			model.addAttribute("error", "アカウントはまだ参照されているため削除できません。");
			model.addAttribute("message", ex.getLocalizedMessage());
    		return showEditUserForm(user, id, page, form, model);
    	}
    	
    	return "redirect:/admin/users?page=" + page;
    }
}
