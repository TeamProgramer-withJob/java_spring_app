package com.example.its.domain.issue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Issue2Form {
    private long id;
    private String summary;
    private String description;
    private String fullpath;
    private int mediaType;
}
