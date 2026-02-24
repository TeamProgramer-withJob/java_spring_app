package com.example.its.domain.mem.controller;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;
import com.example.its.domain.mem.model.MemoryForm;
import com.example.its.domain.mem.service.MemoryService;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Controller
@RequestMapping("/memories")
@RequiredArgsConstructor
public class MemoryController {
	private final MemoryService memoService;
	private final UserService userService;
    
    @GetMapping
    public String showList(Model model) {
    	List<MemoryForm> entities = memoService.findAll();
    	model.addAttribute("memories", entities);

    	return "memories/list";
    }

    @GetMapping("/new")
    public String showNewMemoryForm(@ModelAttribute("data") MemoryForm form) {
        return "memories/creationForm";
    }

    @PostMapping("")
    public String create(@AuthenticationPrincipal UserDetails user, @Validated @ModelAttribute("data") MemoryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showNewMemoryForm(form);
        }
        
        form.setUserId(userService.getUserId(user.getUsername()));
        memoService.save(form);
        return "redirect:/memories";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> serveImage(@PathVariable("id") int id) {
    	EntityMemImage entity = memoService.getImage(id);
    	
    	try {
    		if (entity.getId() > 0) {
    			return ResponseEntity.ok().contentType(MediaType.parseMediaType(entity.getContentType())).body(entity.getImage());
    		} else {
    			return ResponseEntity.notFound().build();
    		}
    	} catch (Exception ex) {
    		return ResponseEntity.internalServerError().build();
    	}
    }

    @PostMapping("/like/{memId}")
    public String setThumbupCount(@AuthenticationPrincipal UserDetails user, @PathVariable("memId") int memId, Model model) {
    	memoService.setThumbup(userService.getUserId(user.getUsername()), memId, 1);
        return showDetail(memId, model);
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("memo", memoService.find(id));
        return "memories/detail";
    }

}
