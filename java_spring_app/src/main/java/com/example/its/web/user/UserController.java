package com.example.its.web.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.auth.User;
import com.example.its.domain.auth.UserService;
import com.example.its.domain.cemetery.CemeteryService;
import com.example.its.domain.follow.FollowService;
import com.example.its.domain.memory.MemoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CemeteryService cemeteryService;
    private final MemoryService memoryService;
    private final FollowService followService;

    /** マイページを表示する */
    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();
        User user = userService.findById(userId);

        model.addAttribute("user",           user);
        model.addAttribute("myCemeteries",   cemeteryService.findByOwnerId(userId));
        model.addAttribute("myMemories",     memoryService.findByAuthorId(userId));
        model.addAttribute("followingCount", followService.countFollowing(userId));
        model.addAttribute("followerCount",  followService.countFollowers(userId));
        return "users/mypage";
    }
}
