package com.example.its.web.cemetery;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.its.domain.auth.CustomUserDetails;
import com.example.its.domain.cemetery.CemeteryService;
import com.example.its.domain.follow.FollowService;
import com.example.its.domain.memory.MemoryService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CemeteryController 回帰テスト
 *
 * <p>GlobalExceptionHandler の例外マッピングを CemeteryController 経由で検証する：
 * <ul>
 *   <li>IllegalArgumentException（霊園が見つからない）→ 404</li>
 *   <li>IllegalStateException（権限なし）→ 403</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class CemeteryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CemeteryService cemeteryService;

    @MockBean
    MemoryService memoryService;

    @MockBean
    FollowService followService;

    /** テスト用ログインユーザー (userId=1) */
    private CustomUserDetails mockUser() {
        return new CustomUserDetails(1L, "testuser", "テストユーザー", "password", List.of());
    }

    /**
     * IllegalArgumentException → 404
     *
     * <p>存在しない霊園IDへのアクセス時に cemeteryService.findById() が例外をスローした場合、
     * GlobalExceptionHandler が 404 を返すこと。
     */
    @Test
    void detail_whenCemeteryNotFound_returns404() throws Exception {
        when(cemeteryService.findById(anyLong()))
                .thenThrow(new IllegalArgumentException("霊園ページが見つかりません (id=999)"));

        mockMvc.perform(get("/cemeteries/999")
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().isNotFound());
    }

    /**
     * IllegalStateException → 403
     *
     * <p>権限のない削除操作で cemeteryService.delete() が例外をスローした場合、
     * GlobalExceptionHandler が 403 を返すこと。
     */
    @Test
    void delete_whenNotOwner_returns403() throws Exception {
        doThrow(new IllegalStateException("削除権限がありません"))
                .when(cemeteryService).delete(anyLong(), anyLong());

        mockMvc.perform(post("/cemeteries/1/delete")
                        .with(Objects.requireNonNull(csrf()))
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().isForbidden());
    }
}
