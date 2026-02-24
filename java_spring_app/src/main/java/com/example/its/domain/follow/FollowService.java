package com.example.its.domain.follow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    /** フォローする */
    @Transactional
    public void follow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("自分自身はフォローできません");
        }
        if (!followRepository.exists(followerId, followeeId)) {
            FollowEntity follow = new FollowEntity();
            follow.setFollowerId(followerId);
            follow.setFolloweeId(followeeId);
            followRepository.insert(follow);
        }
    }

    /** アンフォローする */
    @Transactional
    public void unfollow(Long followerId, Long followeeId) {
        followRepository.delete(followerId, followeeId);
    }

    /** フォロー済みかどうかを返す */
    public boolean isFollowing(Long followerId, Long followeeId) {
        return followRepository.exists(followerId, followeeId);
    }

    /** 指定ユーザーのフォロー中の人数を返す */
    public int countFollowing(Long userId) {
        return followRepository.countFollowing(userId);
    }

    /** 指定ユーザーのフォロワー数を返す */
    public int countFollowers(Long userId) {
        return followRepository.countFollowers(userId);
    }
}
