package com.example.its.domain.mem.controller;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.model.MemoryForm;
import com.example.its.domain.mem.service.MemoryService;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/memories")
@RequiredArgsConstructor
public class MemoryController {
	private final MemoryService memoService;
	private final UserService userService;
    
    @GetMapping
    public String showList(@RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="size", defaultValue = "2") int size, Model model) {
    	Page<MemoryForm> pageObj = memoService.findAll(page, size);
    	model.addAttribute("page", pageObj);

    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = Math.min(pageObj.getTotalPages(), idxStart+2);

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "memories/list";
    }

    @GetMapping("/new")
    public String showNewMemoryForm(@ModelAttribute("data") MemoryForm form, Model model) {
    	if (form.getAct() == null) {
    		form.setAct("new");
    	}
//    	if (!model.containsAttribute("act")) {
//        	model.addAttribute("act", "new");
//    	}
    	return "memories/creationForm";
    }
    @GetMapping("/edit/{id}")
    public String editMemoryPost(@PathVariable("id") int id, Model model) {
    	
    	MemoryForm data = memoService.find(id);
    	data.setAct("update");
    	model.addAttribute("data", data);
    	
    	return showNewMemoryForm(data, model);
    }

    @PostMapping("")
    public String create(@AuthenticationPrincipal UserDetails user, @Validated @ModelAttribute("data") MemoryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showNewMemoryForm(form, model);
        }
        
        form.setUserId(userService.findUser(user.getUsername()).getId());
        memoService.save(form);

        if (form.getAct().equals("image")) {
            return "redirect:/memories/" + form.getId();
        } else {
            return "redirect:/memories";
        }
    }

    @GetMapping("/image/{id}/add")
    public String showAddImage(@PathVariable("id") int id, Model model) {
    	MemoryForm data = memoService.find(id);
    	data.setAct("image");
    	model.addAttribute("data", data);

    	return showNewMemoryForm(data, model);
    }
    @GetMapping("/image/delete/{id}")
    public String deleteImage(@PathVariable("id") int id, Model model) {
    	int memoId = memoService.deleteImage(id);
    	return showDetail(memoId, model);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> renderImage(@PathVariable("id") int id) {
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
    	memoService.setThumbup(userService.findUser(user.getUsername()).getId(), memId, 1);
        return showDetail(memId, model);
    }

    @GetMapping("/delete/{id}")
    public String deletePostedMemory(@PathVariable("id") int id) {
    	memoService.delete(id);
    	return "redirect:/memories";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("memo", memoService.find(id, true, true));
        return "memories/detail";
    }

}
