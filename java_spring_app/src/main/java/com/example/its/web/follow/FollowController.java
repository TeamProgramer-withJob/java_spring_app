package com.example.its.web.follow;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.follow.FollowService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /** フォローする */
    @PostMapping("/follow")
    public String follow(@PathVariable Long userId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         HttpServletRequest request) {
        followService.follow(userDetails.getUserId(), userId);
        return "redirect:" + getPreviousPage(request);
    }

    /** アンフォローする */
    @PostMapping("/unfollow")
    public String unfollow(@PathVariable Long userId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           HttpServletRequest request) {
        followService.unfollow(userDetails.getUserId(), userId);
        return "redirect:" + getPreviousPage(request);
    }

    /** Referer ヘッダーから遷移元ページを取得する（なければ霊園一覧へ） */
    private String getPreviousPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null) ? referer : "/cemeteries";
    }
}
