package com.example.its.web.inquiry;

import com.example.its.domain.inquiry.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InquiryController {

    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    @GetMapping("/contact")
    public String showForm(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "contact_form";
    }

    @PostMapping("/contact")
    public String submitForm(@ModelAttribute Inquiry inquiry) {
        service.save(inquiry);
        return "contact_success";
    }
}
