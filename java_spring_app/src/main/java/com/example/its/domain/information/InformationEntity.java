package com.example.its.domain.information;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InformationEntity {
    private long id;
    private String informationTitle;
    private String informationDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
