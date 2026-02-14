package com.example.its.web.inquiry;

import com.example.its.domain.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("inquiryList", inquiryService.findAll());
        return "inquiries/list";
    }

    @GetMapping("/contactForm")
    public String showForm(@ModelAttribute InquiryForm form) {
        return "inquiries/contactForm";
    }

    @PostMapping
    public String create(@Validated InquiryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "inquiries/contactForm";
        }
        inquiryService.create(form.getName(), form.getEmail(), form.getSubject(), form.getMessage());
        return "redirect:/inquiries/success";
    }

    @GetMapping("/{inquiryId}")
    public String showDetail(@PathVariable("inquiryId") long inquiryId, Model model) {
        model.addAttribute("inquiryId", inquiryService.findById(inquiryId));
        return "inquiries/list";
    }

    @GetMapping("/success")
    public String success() {
        return "inquiries/success";
    }

}
