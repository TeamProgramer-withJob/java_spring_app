package com.example.its.web.contact;

import com.example.its.domain.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/new")
    public String showForm(@ModelAttribute ContactForm form) {
        return "contacts/new";
    }

    @PostMapping("/confirm")
    public String confirm(@Validated ContactForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contacts/new";
        }
        return "contacts/confirm";
    }

    @PostMapping
    public String create(@Validated ContactForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contacts/new";
        }
        contactService.create(form.getName(), form.getEmail(), form.getSubject(), form.getMessage());
        return "redirect:/contacts/finish";
    }

    @GetMapping("/finish")
    public String finish() {
        return "contacts/finish";
    }
}
