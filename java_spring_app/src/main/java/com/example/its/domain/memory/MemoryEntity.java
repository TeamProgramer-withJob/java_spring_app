package com.example.its.domain.memory;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MemoryEntity {

    private Long id;
    private Long cemeteryId;
    private Long authorId;
    private String authorDisplayName;
    private String title;
    private String body;
    private byte[] imageData;
    private String imageContentType;
    private String visibility;
    private LocalDateTime createdAt;
}
