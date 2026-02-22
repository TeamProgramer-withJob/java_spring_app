package com.example.its.domain.memory;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemoryForm {

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 256, message = "タイトルは256文字以内で入力してください")
    private String title;

    @NotBlank(message = "本文は必須です")
    private String body;

    /** 画像ファイル（任意） */
    private MultipartFile image;
}
