package com.example.its.domain.cemetery;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CemeteryService 単体テスト
 *
 * <p>霊園の取得・削除に関するビジネスロジックを Mockito ベースで検証する。
 *
 * <p>検証観点:
 * <ul>
 *   <li>未存在IDへの findById() は IllegalArgumentException をスローすること</li>
 *   <li>非オーナーによる delete() は IllegalStateException をスローすること</li>
 *   <li>オーナーによる delete() は連鎖削除（deleteMemoriesByCemeteryId + deleteById）が実行されること</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class CemeteryServiceTest {

    @Mock
    CemeteryRepository cemeteryRepository;

    @InjectMocks
    CemeteryService cemeteryService;

    /**
     * 未存在IDで findById() → IllegalArgumentException
     *
     * <p>リポジトリが空の Optional を返した場合、例外がスローされること。
     */
    @Test
    void findById_whenNotFound_throwsIllegalArgumentException() {
        when(cemeteryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cemeteryService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("霊園ページが見つかりません");
    }

    /**
     * 非オーナーによる delete() → IllegalStateException
     *
     * <p>霊園は存在するが、リクエストユーザーが所有者でない場合は例外がスローされること。
     */
    @Test
    void delete_whenNotOwner_throwsIllegalStateException() {
        CemeteryEntity cemetery = new CemeteryEntity();
        cemetery.setId(1L);
        cemetery.setOwnerId(99L); // 別のオーナー
        when(cemeteryRepository.findById(1L)).thenReturn(Optional.of(cemetery));

        assertThatThrownBy(() -> cemeteryService.delete(1L, 1L)) // userId=1 はオーナーではない
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("削除権限がありません");
    }

    /**
     * オーナーによる delete() → 連鎖削除が実行される
     *
     * <p>リクエストユーザーが所有者の場合、配下の思い出を先に削除し、次に霊園を削除すること。
     */
    @Test
    void delete_whenOwner_callsCascadeDelete() {
        CemeteryEntity cemetery = new CemeteryEntity();
        cemetery.setId(1L);
        cemetery.setOwnerId(1L); // リクエストユーザーがオーナー
        when(cemeteryRepository.findById(1L)).thenReturn(Optional.of(cemetery));

        cemeteryService.delete(1L, 1L);

        verify(cemeteryRepository).deleteMemoriesByCemeteryId(1L);
        verify(cemeteryRepository).deleteById(1L);
    }
}
