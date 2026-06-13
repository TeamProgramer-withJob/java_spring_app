package com.example.its.domain.contact;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ContactEntity {
    private long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
