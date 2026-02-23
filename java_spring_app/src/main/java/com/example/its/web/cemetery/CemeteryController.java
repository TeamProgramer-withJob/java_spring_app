package com.example.its.web.cemetery;

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

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.cemetery.CemeteryEntity;
import com.example.its.domain.cemetery.CemeteryForm;
import com.example.its.domain.cemetery.CemeteryService;
import com.example.its.domain.follow.FollowService;
import com.example.its.domain.memory.MemoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cemeteries")
@RequiredArgsConstructor
public class CemeteryController {

    private final CemeteryService cemeteryService;
    private final MemoryService memoryService;
    private final FollowService followService;

    /** 霊園一覧 */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("cemeteries", cemeteryService.findAll());
        return "cemeteries/list";
    }

    /** 霊園作成フォーム表示 */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cemeteryForm", new CemeteryForm());
        return "cemeteries/new";
    }

    /** 霊園作成処理 */
    @PostMapping
    public String create(
            @Validated @ModelAttribute CemeteryForm cemeteryForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "cemeteries/new";
        }

        CemeteryEntity created = cemeteryService.create(cemeteryForm, userDetails.getUserId());
        return "redirect:/cemeteries/" + created.getId();
    }

    /** 霊園を削除する（オーナー本人のみ） */
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        cemeteryService.delete(id, userDetails.getUserId());
        return "redirect:/cemeteries";
    }

    /** 霊園詳細 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        CemeteryEntity cemetery = cemeteryService.findById(id);
        Long currentUserId = userDetails.getUserId();
        Long ownerId = cemetery.getOwnerId();

        model.addAttribute("cemetery", cemetery);
        model.addAttribute("isOwner",       ownerId.equals(currentUserId));
        model.addAttribute("memories",      memoryService.findByCemeteryId(id));
        model.addAttribute("isFollowing",   followService.isFollowing(currentUserId, ownerId));
        model.addAttribute("followerCount", followService.countFollowers(ownerId));
        model.addAttribute("followingCount", followService.countFollowing(ownerId));
        return "cemeteries/detail";
    }
}
