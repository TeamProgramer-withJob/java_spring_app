package com.example.its.domain.mem.controller;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.model.MemoryForm;
import com.example.its.domain.mem.service.MemoryService;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public String showList(Pageable pageable, Model model) {
    	Page<MemoryForm> pageObj = memoService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    	model.addAttribute("page", pageObj);

    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = idxStart + pageable.getPageSize();
    	if (idxEnd > pageObj.getTotalPages()) {
    		idxEnd = pageObj.getTotalPages();
    		idxStart = Math.max(1, idxEnd - pageable.getPageSize());
    	}

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "memories/list";
    }

    @GetMapping("/new")
    public String showNewMemoryForm(@RequestParam(name="page", defaultValue="0") int page, @ModelAttribute("data") MemoryForm form, Model model) {
    	if (form.getAct() == null) {
    		form.setAct("new");
    	}
//    	if (!model.containsAttribute("act")) {
//        	model.addAttribute("act", "new");
//    	}
        model.addAttribute("pageNo", page);

        return "memories/creationForm";
    }
    @GetMapping("/edit/{id}")
    public String editMemoryPost(@PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model) {
    	
    	MemoryForm data = memoService.find(id);
    	data.setAct("update");
    	model.addAttribute("data", data);
        model.addAttribute("pageNo", page);
    	
    	return showNewMemoryForm(page, data, model);
    }

    @PostMapping("")
    public String create(@AuthenticationPrincipal UserDetails user, @RequestParam(name="page", defaultValue="0") int page, @Validated @ModelAttribute("data") MemoryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showNewMemoryForm(page, form, model);
        }
        
        form.setUserId(userService.findUser(user.getUsername()).getId());
        memoService.save(form);

        if (form.getAct().equals("image")) {
            return "redirect:/memories/" + form.getId() + "&page=" + page;
        } else {
            return "redirect:/memories" + "?page=" + page;
        }
    }

    @GetMapping("/image/{id}/add")
    public String showAddImage(@PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model) {
    	MemoryForm data = memoService.find(id);
    	data.setAct("image");
    	model.addAttribute("data", data);
        model.addAttribute("pageNo", page);

    	return showNewMemoryForm(page, data, model);
    }
    @GetMapping("/image/delete/{id}")
    public String deleteImage(@PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model) {
    	int memoId = memoService.deleteImage(id);
        model.addAttribute("pageNo", page);

        return showDetail(memoId, page, model);
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
    public String setThumbupCount(@AuthenticationPrincipal UserDetails user, @PathVariable("memId") int memId, @RequestParam(name="page", defaultValue="0") int page, Model model) {
    	memoService.setThumbup(userService.findUser(user.getUsername()).getId(), memId, 1);
        model.addAttribute("pageNo", page);

        return showDetail(memId, page, model);
    }

    @GetMapping("/delete/{id}")
    public String deletePostedMemory(@PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page) {
    	memoService.delete(id);
    	return "redirect:/memories?page=" + page;
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") int id, @RequestParam(name="page", defaultValue="0") int page, Model model) {
        model.addAttribute("memo", memoService.find(id, true, true));
        model.addAttribute("pageNo", page);
        
        return "memories/detail";
    }

}
