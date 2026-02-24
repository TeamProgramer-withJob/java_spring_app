package com.example.its.domain.follow;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FollowEntity {

    private Long followerId;
    private Long followeeId;
    private LocalDateTime createdAt;
}
