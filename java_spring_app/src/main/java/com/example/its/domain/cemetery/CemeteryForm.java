package com.example.its.domain.cemetery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CemeteryForm {

    @NotBlank(message = "霊園名は必須です")
    @Size(max = 100, message = "霊園名は100文字以内で入力してください")
    private String name;

    @Size(max = 1000, message = "説明は1000文字以内で入力してください")
    private String description;
}
