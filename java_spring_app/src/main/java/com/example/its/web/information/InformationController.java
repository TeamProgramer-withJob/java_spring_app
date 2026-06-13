package com.example.its.web.information;

import com.example.its.domain.information.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/informations")
@RequiredArgsConstructor
public class InformationController {

    private final InformationService informationService;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("informationList", informationService.findAll());
        return "informations/list";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute InformationForm form) {
        return "informations/creationForm";
    }

    @PostMapping
    public String create(@Validated InformationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return showCreationForm(form);
        }
        informationService.create(form.getInformationTitle(), form.getInformationDetail());
        return "redirect:/informations";
    }

    @GetMapping("/{informationId}")
    public String showDetail(@PathVariable("informationId") long informationId, Model model) {
        model.addAttribute("information", informationService.findById(informationId));
        return "informations/detail";
    }

    @GetMapping("/{informationId}/editForm")
    public String showEditForm(@PathVariable("informationId") long informationId, Model model) {
        var information = informationService.findById(informationId);
        var form = new InformationForm();
        form.setInformationTitle(information.getInformationTitle());
        form.setInformationDetail(information.getInformationDetail());
        model.addAttribute("informationForm", form);
        model.addAttribute("informationId", informationId);
        return "informations/editForm";
    }

    @PutMapping("/{informationId}")
    public String update(@PathVariable("informationId") long informationId,
                         @Validated InformationForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("informationId", informationId);
            return "informations/editForm";
        }
        informationService.update(informationId, form.getInformationTitle(), form.getInformationDetail());
        return "redirect:/informations/" + informationId;
    }

    @DeleteMapping("/{informationId}")
    public String delete(@PathVariable("informationId") long informationId) {
        informationService.delete(informationId);
        return "redirect:/informations";
    }
}
