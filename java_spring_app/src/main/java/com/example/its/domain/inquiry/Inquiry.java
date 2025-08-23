package com.example.its.domain.inquiry;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inquiry {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}
