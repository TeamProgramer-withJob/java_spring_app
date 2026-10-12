package com.example.its.domain.controller;


import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.its.config.ItsUserDetails;
import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.model.NewsForm;
import com.example.its.domain.model.PulldownIdName;
import com.example.its.domain.service.GenreService;
import com.example.its.domain.service.NewsService;
import com.example.its.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;
	private final UserService userService;
	private final GenreService genreService;
	
    @GetMapping("/news")
    public String listNews(Pageable pageable, @RequestParam(name="all", required = false) String fetchAll, @RequestParam(name="auto", required = false) String autoScroll, Model model) {
    	model.addAttribute("activeMenu", "news");
    	int size = pageable.getPageSize();

    	Page<NewsForm> pageObj = newsService.findAll(pageable.getPageNumber(), size);
    	model.addAttribute("page", pageObj);
    	
    	boolean showAll = fetchAll != null && pageObj.getTotalElements() > size;
    	model.addAttribute("showAll", showAll);
    	model.addAttribute("autoScroll", showAll && autoScroll != null);

    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = idxStart + pageable.getPageSize();
    	if (idxEnd > pageObj.getTotalPages()) {
    		idxEnd = pageObj.getTotalPages();
    		idxStart = Math.max(1, idxEnd - pageable.getPageSize());
    	}
    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "news-list";
    }

    @GetMapping("/news/{id}")
    public String showNewsInformation(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	model.addAttribute("page", page);
    	
    	NewsForm form = newsService.findById(id);
    	if (form.getId() > 0) {
        	model.addAttribute("news", form);
            return "news-detail";
    	} else {
            return "redirect:/news";
    	}
    }

    @GetMapping("/admin/news")
    public String showPostNews(@AuthenticationPrincipal ItsUserDetails user, @ModelAttribute("news") NewsForm form, Model model) {
		model.addAttribute("isEdit", false);

		if (user != null) {
    		EntityUser userE = userService.findUser(user.getUsername());
    		form.setUserId(userE.getId());
    	}
    	model.addAttribute("news", form);

    	List<PulldownIdName> genres = genreService.findAll();
    	model.addAttribute("genres", genres);
    	
        return "news-form";
    }

    @PostMapping("/admin/news")
    public String postNewNews(@AuthenticationPrincipal ItsUserDetails user, @Validated @ModelAttribute("news") NewsForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostNews(null, form, model);
    	}
    	form.setUserId(user.getId());
    	if (form.getValidTill() == null) {
    		form.setValidTill(OffsetDateTime.now());
    	}
    	form.setCreatedAt(OffsetDateTime.now());
    	newsService.registerNews(form);
    	
        return "redirect:/news";
    }

    @GetMapping("/admin/news/edit/{id}")
    public String showEditNews(@AuthenticationPrincipal ItsUserDetails user, @ModelAttribute("news") NewsForm form, Model model) {
		model.addAttribute("isEdit", false);

		if (user != null) {
    		EntityUser userE = userService.findUser(user.getUsername());
    		form.setUserId(userE.getId());
    	}
    	
        return "news-form";
    }

    @PutMapping("admin/news/edit/{id}")
    public String updateNews(@Validated @ModelAttribute("news") NewsForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostNews(null, form, model);
    	}
    	
    	newsService.registerNews(form);
    	
        return "redirect:/news";
    }

    @GetMapping("/admin/news/delete/{id}")
    public String deleteNews(@AuthenticationPrincipal ItsUserDetails user, @PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	NewsForm form = newsService.findById(id);
    	if (form.getId() > 0) {
    		newsService.deleteNews(id);
    	}

    	return "redirect:/news?page=" + page;
    }
}
