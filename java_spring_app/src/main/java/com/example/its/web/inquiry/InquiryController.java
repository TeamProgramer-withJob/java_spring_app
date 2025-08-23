package com.example.its.web.inquiry;

import com.example.its.domain.inquiry.Inquiry;
import com.example.its.domain.inquiry.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/contact")
public class InquiryController {

    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "inquiries/contactForm";
    }

    @PostMapping
    public String submitForm(@ModelAttribute @Valid Inquiry inquiry, BindingResult binding) {
        if (binding.hasErrors()) {
            return "inquiries/contactForm";
        }
        service.save(inquiry);                  // ← JPAでINSERT
        return "redirect:/contact/success";
    }

    @GetMapping("/success")
    public String success() {
        return "inquiries/contactSuccess";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", service.findAll());
        return "inquiries/list";
    }

}
