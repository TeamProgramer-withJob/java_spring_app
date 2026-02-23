package com.example.its.web.follow;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.follow.FollowService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FollowController 回帰テスト
 *
 * <p>修正Cの動作を検証する：
 * <ul>
 *   <li>C: オープンリダイレクト対策 — 不正Refererは /cemeteries にフォールバック</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class FollowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FollowService followService;

    /** テスト用ログインユーザー (userId=1) */
    private CustomUserDetails mockUser() {
        return new CustomUserDetails(1L, "testuser", "テストユーザー", "password", List.of());
    }

    /**
     * 修正C: 外部URLをRefererに設定した場合 → /cemeteries へフォールバック
     *
     * <p>Referer に外部サイトURLを設定してフォローリクエストを送信した場合、
     * オープンリダイレクトは発生せず /cemeteries にリダイレクトされること。
     */
    @Test
    void follow_withEvilReferer_redirectsToCemeteries() throws Exception {
        mockMvc.perform(post("/users/2/follow")
                        .with(Objects.requireNonNull(csrf()))
                        .with(Objects.requireNonNull(user(mockUser())))
                        .header("Referer", "https://evil.example.com/steal"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cemeteries"));
    }

    /**
     * 修正C: Refererが欠落している場合 → /cemeteries へフォールバック
     *
     * <p>Referer ヘッダーが存在しない場合も /cemeteries にリダイレクトされること。
     */
    @Test
    void follow_withNoReferer_redirectsToCemeteries() throws Exception {
        mockMvc.perform(post("/users/2/follow")
                        .with(Objects.requireNonNull(csrf()))
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cemeteries"));
    }

    /**
     * 修正C: 正規のReferer（/cemeteries/{id}）の場合 → そのパスにリダイレクト
     *
     * <p>自アプリ内の許可パスをRefererに設定した場合は、そのパスにリダイレクトされること。
     */
    @Test
    void follow_withValidReferer_redirectsToRefererPath() throws Exception {
        mockMvc.perform(post("/users/2/follow")
                        .with(Objects.requireNonNull(csrf()))
                        .with(Objects.requireNonNull(user(mockUser())))
                        .header("Referer", "http://localhost:8080/cemeteries/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cemeteries/5"));
    }
}
