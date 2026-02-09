package com.example.its.domain.issue.model;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class IssueForm {

    @NotBlank(message="必須入力項目です。")
    @Size(max=256)
    private String summary;

    @NotBlank
    @Size(max=256)
    private String description;

}
