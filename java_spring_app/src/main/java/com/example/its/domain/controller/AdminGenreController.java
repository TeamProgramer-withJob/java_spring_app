package com.example.its.domain.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.example.its.domain.entity.EntityGenre;
import com.example.its.domain.model.GenreForm;
import com.example.its.domain.service.GenreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/genres")
@RequiredArgsConstructor
public class AdminGenreController {
	private final GenreService genreService;
	
    @GetMapping()
    public String listGenres(Pageable pageable, Model model) {
    	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
    	Page<EntityGenre> pageObj = genreService.findAll(pageable);
    	model.addAttribute("page", pageObj);
    	
    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = idxStart + pageable.getPageSize();
    	if (idxEnd > pageObj.getTotalPages()) {
    		idxEnd = pageObj.getTotalPages();
    		idxStart = Math.max(1, idxEnd - pageable.getPageSize());
    	}

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "admin/genre-list";
    }

    @GetMapping("/{id}")
    public String showGenreDetail(@PathVariable("id") int id, @RequestParam("page") int page, GenreForm genre, Model model) {
    	model.addAttribute("pageNo", page);
    	
    	GenreForm form = genreService.findById(id);
    	if (form.getId() > 0) {
        	model.addAttribute("genre", form);
            return "genre-detail";
    	} else {
            return "redirect:/admin/genres";
    	}
    }

    @GetMapping("/new")
    public String showPostGenre(@ModelAttribute("genre") GenreForm form, Model model) {
		model.addAttribute("isEdit", false);
    	
        return "admin/genre-form";
    }
    
    @PostMapping()
    public String createGenre(@Validated @ModelAttribute("genre") GenreForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostGenre(form, model);
    	}
    	
    	genreService.registerGenre(form);
    	
        return "redirect:/admin/genres";
    }

    @GetMapping("/edit/{id}")
    public String showEditGenre(@PathVariable("id") int id, @RequestParam("page") int page, Model model) {
		model.addAttribute("isEdit", true);
		model.addAttribute("page", page);
    	
		model.addAttribute("genre", genreService.findById(id));

		return "admin/genre-form";
    }
    
    @PutMapping("/edit/{id}")
    public String updateGenre(@PathVariable("id") int id, @RequestParam("page") int page, @Validated @ModelAttribute("genre") GenreForm form, BindingResult result, Model model) {
		model.addAttribute("isEdit", true);
		model.addAttribute("page", page);

		if (result.hasErrors()) {
			model.addAttribute("genre", form);
			
			return "admin/genre-form";
    	} else {
    		GenreForm tForm = genreService.findById(id);
    		if (tForm.getId() > 0) {
    			form.setCreatedAt(tForm.getCreatedAt());
            	genreService.updateGenre(form);

            	return "redirect:/admin/genres?page=" + page;
    		} else {
    			model.addAttribute("genre", form);
    			model.addAttribute("error", "ジャンル見つかりません。");
    			
    			return "admin/genre-form";
    		}
    	}
    }

    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	GenreForm form = genreService.findById(id);
    	if (form.getId() > 0) {
    		genreService.deleteGenre(id);
    	}
		return "redirect:/admin/genres?page=" + page;
    }
}
