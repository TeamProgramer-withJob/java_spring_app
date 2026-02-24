package com.example.its.domain.follow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * FollowService 単体テスト
 *
 * <p>フォロー/アンフォローのビジネスロジックを Mockito ベースで検証する。
 *
 * <p>検証観点:
 * <ul>
 *   <li>自分自身フォロー時に IllegalArgumentException がスローされること</li>
 *   <li>未フォロー時は followRepository.insert() が呼ばれること</li>
 *   <li>フォロー済み時は followRepository.insert() が呼ばれないこと</li>
 *   <li>アンフォロー時は followRepository.delete() が呼ばれること</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    FollowRepository followRepository;

    @InjectMocks
    FollowService followService;

    /**
     * 自分自身フォロー時は IllegalArgumentException
     *
     * <p>followerId と followeeId が同一の場合、DB アクセスなしで例外がスローされること。
     */
    @Test
    void follow_selfFollow_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> followService.follow(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("自分自身はフォローできません");

        // DB アクセスが発生していないことを確認
        verify(followRepository, never()).exists(any(), any());
        verify(followRepository, never()).insert(any());
    }

    /**
     * 未フォロー時は insert が呼ばれる
     *
     * <p>followRepository.exists() が false を返す場合、followRepository.insert() が呼ばれること。
     */
    @Test
    void follow_whenNotFollowing_callsInsert() {
        when(followRepository.exists(1L, 2L)).thenReturn(false);

        followService.follow(1L, 2L);

        verify(followRepository).insert(any(FollowEntity.class));
    }

    /**
     * フォロー済み時は insert されない
     *
     * <p>followRepository.exists() が true を返す場合、followRepository.insert() が呼ばれないこと。
     * 重複フォロー防止の動作を検証する。
     */
    @Test
    void follow_whenAlreadyFollowing_doesNotCallInsert() {
        when(followRepository.exists(1L, 2L)).thenReturn(true);

        followService.follow(1L, 2L);

        verify(followRepository, never()).insert(any());
    }

    /**
     * アンフォロー時は delete が呼ばれる
     *
     * <p>unfollow() 実行時に followRepository.delete() が正しい引数で呼ばれること。
     */
    @Test
    void unfollow_callsDelete() {
        followService.unfollow(1L, 2L);

        verify(followRepository).delete(eq(1L), eq(2L));
    }
}
