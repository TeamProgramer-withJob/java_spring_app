package com.example.its.domain.model;

import java.time.OffsetDateTime;

import com.example.its.domain.entity.EntityGenre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GenreForm {
	private int id;
	
	@NotBlank(message = "必須項目です。")
	@Size(max = 128)
	private String title;
	
	private OffsetDateTime createdAt;

	public GenreForm(EntityGenre entity) {
		id = entity.getId();
		title = entity.getTitle();
		createdAt = entity.getCreatedAt();
	}
}
