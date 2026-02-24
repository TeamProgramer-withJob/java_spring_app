package com.example.its.web.follow;

import java.net.URI;

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
        return "redirect:" + safeRedirectPath(request);
    }

    /** アンフォローする */
    @PostMapping("/unfollow")
    public String unfollow(@PathVariable Long userId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           HttpServletRequest request) {
        followService.unfollow(userDetails.getUserId(), userId);
        return "redirect:" + safeRedirectPath(request);
    }

    /**
     * Referer ヘッダーから安全なリダイレクト先パスを返す。
     *
     * <p>オープンリダイレクト対策として、パス部分のみを抽出し、
     * 自アプリ内の許可パスに一致する場合のみ使用する。
     * 許可外・不正・欠落時は /cemeteries にフォールバックする。
     */
    private static final String FALLBACK = "/cemeteries";

    private String safeRedirectPath(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return FALLBACK;
        }
        try {
            String path = URI.create(referer).getPath();
            if (isAllowedPath(path)) {
                return path;
            }
        } catch (IllegalArgumentException e) {
            // 不正な URI はフォールバック
        }
        return FALLBACK;
    }

    /** 自アプリ内の許可パスか検証する */
    private boolean isAllowedPath(String path) {
        if (path == null) {
            return false;
        }
        return path.equals("/cemeteries")
                || path.startsWith("/cemeteries/")
                || path.equals("/mypage");
    }
}
