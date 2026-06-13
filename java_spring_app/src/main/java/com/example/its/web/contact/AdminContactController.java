package com.example.its.web.contact;

import com.example.its.domain.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactService contactService;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("contactList", contactService.findAll());
        return "admin/contacts/list";
    }

    @GetMapping("/{contactId}")
    public String showDetail(@PathVariable("contactId") long contactId, Model model) {
        model.addAttribute("contact", contactService.findById(contactId));
        return "admin/contacts/detail";
    }

    @DeleteMapping("/{contactId}")
    public String delete(@PathVariable("contactId") long contactId) {
        contactService.delete(contactId);
        return "redirect:/admin/contacts";
    }
}
