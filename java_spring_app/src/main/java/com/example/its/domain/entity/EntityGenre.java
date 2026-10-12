package com.example.its.domain.entity;

import java.time.OffsetDateTime;

import com.example.its.domain.model.GenreForm;

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
@Table(name = "genres")
@ToString
@Data
@RequiredArgsConstructor
public class EntityGenre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title", nullable = false, length = 128)
	private String title;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createdAt;
	
	public EntityGenre(GenreForm form) {
		id = form.getId();
		title = form.getTitle();
		if (form.getCreatedAt() != null) createdAt = form.getCreatedAt();
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now();
		}
	}
}
