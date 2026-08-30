package com.example.its.domain.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.its.config.ItsUserDetails;
import com.example.its.domain.annot.OnCreate;
import com.example.its.domain.annot.OnEdit;
import com.example.its.domain.entity.EntityInquiry;
import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.model.InquiryForm;
import com.example.its.domain.service.InquiryService;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;
	private final UserService userService;
	
    @GetMapping()
    public String listInquiries(Pageable pageable, Model model) {
    	Page<EntityInquiry> pageObj = inquiryService.findAll(pageable);
    	model.addAttribute("page", pageObj);
    	
    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = Math.min(pageObj.getTotalPages(), idxStart + pageable.getPageSize());

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "inquiry-list";
    }
    @GetMapping("/new")
    public String showPostInquiry(@AuthenticationPrincipal UserDetails user, @ModelAttribute("inquiry") InquiryForm form, Model model) {
    	if (user != null) {
    		EntityUser userE = userService.findUser(user.getUsername());
    		form.setName(userE.getName());
    		form.setEmail(userE.getEmail());
    		
    		model.addAttribute("isEdit", false);
    	}
    	
        return "inquiry-form";
    }
    @PostMapping()
    public String createInquiry(@Validated(OnCreate.class) @ModelAttribute("inquiry") InquiryForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostInquiry(null, form, model);
    	}
    	
    	inquiryService.registerInquiry(form);
    	
        return "redirect:/inquiries";
    }
    @GetMapping("/{id}")
    public String showInquiryDetail(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, InquiryForm inquiry, Model model) {
    	model.addAttribute("pageNo", page);
    	
    	if (user == null) {
    		// ログインしていない
    		model.addAttribute("inquiry", inquiry);
            return "inquiry-email";
    	} else {
    		// ログイン中
        	InquiryForm form = inquiryService.findById(id);
        	if (form.getId() > 0 && (user.isAdmin() || form.getEmail().equalsIgnoreCase(user.getUsername()))) {
        		// authorized
            	model.addAttribute("inquiry", form);
                return "inquiry-detail";
        	} else {
        		// not allowed
        		model.addAttribute("inquiry", inquiry);
            	model.addAttribute("error", true);
        		model.addAttribute("message", "他人の投稿内容は表示できません。");
                return "inquiry-email";
        	}
    	}
    }
    @PostMapping("/email/{id}")
    public String verifyInquiryEmail(@PathVariable("id") int id, @RequestParam("page") int page, @ModelAttribute("inquiry") InquiryForm form, BindingResult result, Model model) {
    	InquiryForm inquiry = inquiryService.findById(id);
    	if (inquiry.getId() > 0 && inquiry.getEmail().equalsIgnoreCase(form.getEmail())) {
        	model.addAttribute("pageNo", page);
        	model.addAttribute("inquiry", inquiry);
            return "inquiry-detail";
    	} else {
    		// inquiry not found or not allowed
    	}
    	
        return "redirect:/inquiries";
    }
    @GetMapping("/edit/{id}")
    public String showEditInquiry(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	model.addAttribute("pageNo", page);
    	
    	if (user == null) {
    		// ログインしていない
            return "redirect:/login?error";
    	} else {
        	InquiryForm form = inquiryService.findById(id);
        	model.addAttribute("inquiry", form);

        	if (form.getId() > 0 && (user.isAdmin() || form.getEmail().equalsIgnoreCase(user.getUsername()))) {
        		// authorized
        		model.addAttribute("isEdit", true);

        		return "inquiry-form";
        	} else {
        		// not allowed
            	model.addAttribute("error", true);
        		model.addAttribute("message", "他人の投稿内容は編集できません。");
                return "inquiry-email";
        	}
    	}
    }
    @PutMapping("/edit/{id}")
    public String authAndFetchInquiryDetail(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, 
    		                                @Validated(OnEdit.class) @ModelAttribute("inquiry") InquiryForm inquiry, BindingResult result, Model model) {
    	if (user == null) {
    		// ログインしていない
            return "redirect:/login?error";
    	} else if (result.hasErrors()) {
        	model.addAttribute("inquiry", inquiry);
    		model.addAttribute("isEdit", true);

    		return "inquiry-form";
    	} else {
        	InquiryForm form = inquiryService.findById(id);
        	if (form.getId() > 0 && (user.isAdmin() || form.getEmail().equalsIgnoreCase(user.getUsername()))) {
        		// update inquiry
        		inquiry.setName(form.getName());
        		inquiry.setEmail(form.getEmail());
        		inquiryService.updateInquiry(inquiry);
        		return "redirect:/inquiries?page=0";
        	} else {
        		// not allowed
            	model.addAttribute("error", true);
        		model.addAttribute("message", "他人の投稿内容は編集できません。");
            	model.addAttribute("pageNo", page);

                return "inquiry-email";
        	}
    	}
    }
    @GetMapping("/delete/{id}")
    public String deleteInquiry(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	if (user == null) {
    		// ログインしていない
            return "redirect:/login?error";
    	} else {
        	InquiryForm form = inquiryService.findById(id);
        	if (form.getId() > 0 && (user.isAdmin() || form.getEmail().equalsIgnoreCase(user.getUsername()))) {
        		// delete inquiry
        		inquiryService.deleteInquiry(id);

        		return "redirect:/inquiries?page=0";
        	} else {
        		// not allowed
            	model.addAttribute("error", true);
        		model.addAttribute("message", "他人の投稿は削除できません。");
            	model.addAttribute("pageNo", page);
            	model.addAttribute("inquiry", form);

                return "inquiry-email";
        	}
    	}
    }
    
}
