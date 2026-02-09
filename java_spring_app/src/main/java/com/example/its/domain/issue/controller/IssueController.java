package com.example.its.domain.issue.controller;

import com.example.its.domain.issue.entity.EntityIssue;
import com.example.its.domain.issue.model.Issue2Form;
import com.example.its.domain.issue.model.IssuePostForm;
import com.example.its.domain.issue.service.Issue2Service;
import com.example.its.domain.issue.service.IssueService;

import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final Issue2Service issue2Service;
    
    @GetMapping
    public String showList(Model model) {
    	// get issue list by ibatis Mapper
//        model.addAttribute("issueList", issueService.findAll());
    	// get issue list by JPA
    	List<EntityIssue> entities = issue2Service.findAll2();
    	for (EntityIssue issue: entities) {
    		if (issue.getMediaType() > 0) {
        		Path fullpath = Paths.get(issue.getFullpath());
        		issue.setFullpath(fullpath.getFileName().toString());
    		} else {
        		issue.setFullpath("");
    		}
    	}
    	model.addAttribute("issueList", entities);

    	return "issues/list";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute IssuePostForm form) {
        return "issues/creationForm";
    }

    @PostMapping("")
    public String create(@Validated IssuePostForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showCreationForm(form);
        }
        
        String fullpath = null;
        int mediaType = -1;
        try {
        	Path wDir = Files.createDirectories(Paths.get("images", UUID.randomUUID().toString()));
        	String filename = form.getFile().getOriginalFilename();
        	Path tmpFullpath = Paths.get(wDir.toString(), filename);
        	Files.copy(form.getFile().getInputStream(), tmpFullpath, StandardCopyOption.REPLACE_EXISTING);
        	
        	fullpath = tmpFullpath.toString();
        	switch (form.getFile().getContentType().toLowerCase()) {
        	case "image/jpeg":
        		mediaType = 1;
        		break;
        	case "image/png":
        		mediaType = 2;
        		break;
        	default:
        		
        	}
        } catch (Exception ex) {
        	
        }

        issueService.create(form.getSummary(), form.getDescription(), fullpath, mediaType);
        return "redirect:/issues";
    }

    @GetMapping("/{issueId}")
    public String showDetail(@PathVariable("issueId") long issueId, Model model) {
        model.addAttribute("issue", issueService.findById(issueId));
        return "issues/detail";
    }

    @GetMapping("/image/{issueId}")
    public ResponseEntity<Resource> serveImage(@PathVariable("issueId") long issueId) {
    	Issue2Form entity = issueService.findById(issueId);
    	
    	try {
    		Path file = Paths.get(entity.getFullpath());
    		Resource resource = new UrlResource(file.toUri());

    		if (resource.exists() || resource.isReadable()) {
    			MediaType mediaType = entity.getMediaType()==1 ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
    			return ResponseEntity.ok().contentType(mediaType).body(resource);
    		} else {
    			return ResponseEntity.notFound().build();
    		}
    	} catch (Exception ex) {
    		return ResponseEntity.internalServerError().build();
    	}
    }
}
