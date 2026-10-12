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

import com.example.its.domain.entity.EntityPlan;
import com.example.its.domain.model.PlanForm;
import com.example.its.domain.service.PlanService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/plans")
@RequiredArgsConstructor
public class AdminPlanController {
	private final PlanService planService;
	
    @GetMapping()
    public String listPlans(Pageable pageable, Model model) {
    	// set sort by id asc
    	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
    	Page<EntityPlan> pageObj = planService.findAll(pageable);
    	model.addAttribute("page", pageObj);
    	
    	int idxStart = Math.max(1, pageObj.getNumber());
    	int idxEnd = Math.min(pageObj.getTotalPages(), idxStart + pageable.getPageSize());

    	model.addAttribute("idxStart", idxStart);
    	model.addAttribute("idxEnd", idxEnd);

    	return "admin/plan-list";
    }

    @GetMapping("/{id}")
    public String showPlanDetail(@PathVariable("id") int id, @RequestParam("page") int page, PlanForm plan, Model model) {
    	model.addAttribute("pageNo", page);
    	
    	PlanForm form = planService.findById(id);
    	if (form.getId() > 0) {
        	model.addAttribute("plan", form);
            return "plan-detail";
    	} else {
            return "redirect:/admin/plans";
    	}
    }

    @GetMapping("/new")
    public String showPostPlan(@ModelAttribute("plan") PlanForm form, Model model) {
		model.addAttribute("isEdit", false);
    	
        return "admin/plan-form";
    }
    
    @PostMapping()
    public String createPlan(@Validated @ModelAttribute("plan") PlanForm form, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return showPostPlan(form, model);
    	}
    	
    	planService.registerPlan(form);
    	
        return "redirect:/admin/plans";
    }

    @GetMapping("/edit/{id}")
    public String showEditGenre(@PathVariable("id") int id, @RequestParam("page") int page, Model model) {
		model.addAttribute("isEdit", true);
		model.addAttribute("page", page);
    	
		model.addAttribute("plan", planService.findById(id));

		return "admin/plan-form";
    }
    
    @PutMapping("/edit/{id}")
    public String updateGenre(@PathVariable("id") int id, @RequestParam("page") int page, @Validated @ModelAttribute("plan") PlanForm form, BindingResult result, Model model) {
		model.addAttribute("isEdit", true);
		model.addAttribute("page", page);

		if (result.hasErrors()) {
			model.addAttribute("plan", form);

			return "admin/plan-form";
    	} else {
    		PlanForm tForm = planService.findById(id);
    		if (tForm.getId() > 0) {
    			form.setCreatedAt(tForm.getCreatedAt());
            	planService.updatePlan(form);
            	
                return "redirect:/admin/plans?page=" + page;
    		} else {
    			model.addAttribute("error", "料金プランは見つかりません。");
    			model.addAttribute("plan", form);

    			return "admin/plan-form";
    		}
    	}
    }

    @GetMapping("/delete/{id}")
    public String deletePlan(@PathVariable("id") int id, @RequestParam("page") int page, Model model) {
    	PlanForm form = planService.findById(id);
    	if (form.getId() > 0) {
    		planService.deletePlan(id);
    	}
		return "redirect:/admin/plans?page=" + page;
    }
}
