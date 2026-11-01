package com.example.its.domain.controller;


import java.time.OffsetDateTime;
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
import com.example.its.domain.model.GenreForm;
import com.example.its.domain.model.NewsForm;
import com.example.its.domain.model.PulldownIdName;
import com.example.its.domain.service.GenreService;
import com.example.its.domain.service.NewsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;
	private final GenreService genreService;
	
    @GetMapping("/news")
    public String showNewsHome(@RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="all", defaultValue = "false") Boolean fetchAll, @RequestParam(name="auto", defaultValue = "false") Boolean autoScroll, Model model) {
    	model.addAttribute("activeMenu", "news");
    	model.addAttribute("pageNo", page);
    	model.addAttribute("all", fetchAll);
    	model.addAttribute("auto", autoScroll);

    	return "news-home";
    }

    @GetMapping("/news/detail/{id}")
    public String showNewsInformation(@PathVariable("id") int id, Model model) {
    	NewsForm form = newsService.findById(id);
    	model.addAttribute("news", form);

    	GenreForm genre = genreService.findById(form.getGenreId());
    	model.addAttribute("genre", "【" + genre.getTitle() + "】");

    	return "news-detail";
    }

    @GetMapping("/news/list")
    public String listNews(Pageable pageable, @RequestParam(name="all", defaultValue = "false") Boolean fetchAll, @RequestParam(name="auto", defaultValue = "false") Boolean autoScroll, Model model) {
    	model.addAttribute("activeMenu", "news");
    	int size = pageable.getPageSize();

    	Page<NewsForm> pageObj = newsService.findAll(pageable.getPageNumber(), size);
    	model.addAttribute("page", pageObj);
    	model.addAttribute("pageNo", pageable.getPageNumber());
    	
    	boolean showAll = fetchAll && pageObj.getTotalElements() > size;
    	model.addAttribute("showAll", showAll);
    	model.addAttribute("auto", autoScroll);

    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = idxStart + pageable.getPageSize();
    	if (idxEnd > pageObj.getTotalPages()) {
    		idxEnd = pageObj.getTotalPages();
    		idxStart = Math.max(1, idxEnd - pageable.getPageSize());
    	}
    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "news-list::newsList";
    }

    @GetMapping("/admin/news")
    public String showPostNews(@ModelAttribute("news") NewsForm form, Model model) {
		model.addAttribute("isEdit", false);

		if (form.getValidTill() == null) form.setValidTill(OffsetDateTime.now().plusDays(90).toLocalDate());
    	model.addAttribute("news", form);

    	List<PulldownIdName> genres = genreService.findAll();
    	model.addAttribute("genres", genres);
    	
        return "news-form";
    }

    @PostMapping("/admin/news")
    public String postNewNews(@AuthenticationPrincipal ItsUserDetails user, @Validated @ModelAttribute("news") NewsForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostNews(form, model);
    	}
    	form.setUserId(user.getId());
    	form.setCreatedAt(OffsetDateTime.now());
    	newsService.registerNews(form);
    	
        return "redirect:/news?all=true&auto=true";
    }

    @GetMapping("/admin/news/edit/{id}")
    public String showEditNews(@RequestParam(name="page") int page, @RequestParam(name="all") Boolean fetchAll, @ModelAttribute("news") NewsForm form, Model model) {
		model.addAttribute("isEdit", true);
		model.addAttribute("all", fetchAll);
		model.addAttribute("page", page);

		form = newsService.findById(form.getId());
    	model.addAttribute("news", form);

    	List<PulldownIdName> genres = genreService.findAll();
    	model.addAttribute("genres", genres);

    	return "news-form";
    }

    @PutMapping("admin/news/edit/{id}")
    public String updateNews(@AuthenticationPrincipal ItsUserDetails user, @RequestParam(name="page") int page, @RequestParam(name="all") Boolean fetchAll, @Validated @ModelAttribute("news") NewsForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showEditNews(page, fetchAll, form, model);
    	}
    	
    	form.setUserId(user.getId());
    	NewsForm tForm = newsService.findById(form.getId());
    	form.setCreatedAt(tForm.getCreatedAt());
    	newsService.registerNews(form);
    	
        return "redirect:/news?auto=true&page=" + page + "&all=" + fetchAll;
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
