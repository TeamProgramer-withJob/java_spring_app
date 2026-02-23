package com.example.its.web.memory;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.cemetery.CemeteryEntity;
import com.example.its.domain.cemetery.CemeteryService;
import com.example.its.domain.memory.MemoryEntity;
import com.example.its.domain.memory.MemoryForm;
import com.example.its.domain.memory.MemoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cemeteries/{cemeteryId}/memories")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;
    private final CemeteryService cemeteryService;

    /** 思い出投稿フォームを表示する */
    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long cemeteryId, Model model) {
        CemeteryEntity cemetery = cemeteryService.findById(cemeteryId);
        model.addAttribute("cemetery", cemetery);
        model.addAttribute("memoryForm", new MemoryForm());
        return "memories/new";
    }

    /** 思い出を投稿する */
    @PostMapping
    public String create(
            @PathVariable Long cemeteryId,
            @Validated @ModelAttribute MemoryForm memoryForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cemetery", cemeteryService.findById(cemeteryId));
            return "memories/new";
        }

        MemoryEntity created = memoryService.create(memoryForm, cemeteryId, userDetails.getUserId());
        return "redirect:/cemeteries/" + cemeteryId + "/memories/" + created.getId();
    }

    /** 思い出詳細を表示する */
    @GetMapping("/{memoryId}")
    public String detail(@PathVariable Long cemeteryId, @PathVariable Long memoryId,
                         Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemoryEntity memory = memoryService.findById(memoryId);
        model.addAttribute("memory", memory);
        model.addAttribute("cemetery", cemeteryService.findById(cemeteryId));
        model.addAttribute("isAuthor", memory.getAuthorId().equals(userDetails.getUserId()));
        return "memories/detail";
    }

    /** 思い出を削除する（投稿者本人のみ） */
    @PostMapping("/{memoryId}/delete")
    public String delete(
            @PathVariable Long cemeteryId,
            @PathVariable Long memoryId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        memoryService.delete(memoryId, userDetails.getUserId());
        return "redirect:/cemeteries/" + cemeteryId;
    }

    /** 思い出に添付された画像を配信する */
    @GetMapping("/{memoryId}/image")
    @ResponseBody
    public ResponseEntity<byte[]> image(@PathVariable Long cemeteryId, @PathVariable Long memoryId) {
        MemoryEntity imageEntity = memoryService.findImageById(memoryId);
        if (imageEntity.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageEntity.getImageContentType()))
                .body(imageEntity.getImageData());
    }
}
