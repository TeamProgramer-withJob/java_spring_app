package com.example.its.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.example.its.domain.entity.EntityNews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NewsForm {
	private int id;
	
	@NotBlank(message = "必須項目です。")
	@Size(max = 128)
	private String title;
	
	@Size(max = 2048)
	private String information;

	private int genreId;
	private int userId;
	private LocalDate validTill;
	private OffsetDateTime createdAt;

	public NewsForm(EntityNews entity) {
		id = entity.getId();
		title = entity.getTitle();
		information = entity.getInformation();
		genreId = entity.getGenreId();
		userId = entity.getUserId();
		OffsetDateTime tValidTill = entity.getValidTill();
		validTill = tValidTill == null ? null: tValidTill.toLocalDate();
		createdAt = entity.getCreatedAt();
	}
}
