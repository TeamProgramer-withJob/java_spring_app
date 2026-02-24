package com.example.its.domain.cemetery;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CemeteryEntity {

    private Long id;
    private Long ownerId;
    private String ownerDisplayName;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
