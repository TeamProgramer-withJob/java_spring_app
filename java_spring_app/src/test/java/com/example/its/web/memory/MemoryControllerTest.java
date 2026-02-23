package com.example.its.web.memory;

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
import com.example.its.domain.memory.MemoryEntity;
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
 * MemoryController 回帰テスト
 *
 * <p>修正A/Bの動作を検証する：
 * <ul>
 *   <li>A: GlobalExceptionHandler — IllegalArgumentException→404, IllegalStateException→403</li>
 *   <li>B: パス整合性検証 — memory.cemeteryId != path.cemeteryId → 404</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class MemoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemoryService memoryService;

    @MockBean
    CemeteryService cemeteryService;

    /** テスト用ログインユーザー (userId=1) */
    private CustomUserDetails mockUser() {
        return new CustomUserDetails(1L, "testuser", "テストユーザー", "password", List.of());
    }

    /**
     * 修正A: IllegalArgumentException → 404
     *
     * <p>存在しないIDへのアクセス時に memoryService.findById() が例外をスローした場合、
     * GlobalExceptionHandler が 404 を返すこと。
     */
    @Test
    void detail_whenMemoryNotFound_returns404() throws Exception {
        when(memoryService.findById(anyLong()))
                .thenThrow(new IllegalArgumentException("思い出が見つかりません (id=999)"));

        mockMvc.perform(get("/cemeteries/1/memories/999")
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().isNotFound());
    }

    /**
     * 修正A: IllegalStateException → 403
     *
     * <p>権限のない削除操作で memoryService.delete() が例外をスローした場合、
     * GlobalExceptionHandler が 403 を返すこと。
     */
    @Test
    void delete_whenNoPermission_returns403() throws Exception {
        MemoryEntity memory = new MemoryEntity();
        memory.setId(1L);
        memory.setCemeteryId(1L);
        memory.setAuthorId(99L); // 別ユーザーの投稿
        when(memoryService.findById(1L)).thenReturn(memory);
        doThrow(new IllegalStateException("削除権限がありません"))
                .when(memoryService).delete(anyLong(), anyLong());

        mockMvc.perform(post("/cemeteries/1/memories/1/delete")
                        .with(Objects.requireNonNull(csrf()))
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().isForbidden());
    }

    /**
     * 修正B: パスのcemeteryIdとmemory.cemeteryIdが不一致 → 404
     *
     * <p>URLの {cemeteryId} と取得した memory.cemeteryId が一致しない場合、
     * validateMemoryBelongsToCemetery() が例外をスローし、404 が返ること。
     */
    @Test
    void detail_whenMemoryNotBelongToCemetery_returns404() throws Exception {
        MemoryEntity memory = new MemoryEntity();
        memory.setId(1L);
        memory.setCemeteryId(99L); // path の cemeteryId=1 と不一致
        memory.setAuthorId(1L);
        when(memoryService.findById(1L)).thenReturn(memory);

        mockMvc.perform(get("/cemeteries/1/memories/1")
                        .with(Objects.requireNonNull(user(mockUser()))))
                .andExpect(status().isNotFound());
    }
}
