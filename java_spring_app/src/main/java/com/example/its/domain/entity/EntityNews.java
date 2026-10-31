package com.example.its.domain.entity;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.example.its.domain.model.NewsForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "news")
@ToString
@Data
@RequiredArgsConstructor
public class EntityNews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title", nullable = false, length = 128)
	private String title;

	@Column(name = "information", nullable = true, length = 1024)
	private String information;

	@Column(name = "genre_id", nullable = false)
	private int genreId;

	@Column(name = "user_id", nullable = false)
	private int userId;

	@Column(name = "valid_till", nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime validTill;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createdAt;
	
	public EntityNews(NewsForm form) {
		id = form.getId();
		title = form.getTitle();
		information = form.getInformation();
		genreId = form.getGenreId();
		userId = form.getUserId();
		validTill = form.getValidTill().atStartOfDay(ZoneId.of("Asia/Tokyo")).toOffsetDateTime();
		createdAt = form.getCreatedAt();
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now();
		}
	}
}
