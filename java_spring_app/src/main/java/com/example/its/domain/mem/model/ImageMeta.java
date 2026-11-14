package com.example.its.domain.mem.model;

import java.time.OffsetDateTime;

import com.example.its.domain.mem.entity.EntityMemImage;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImageMeta {
	public record Sentiment(int id, int userId) {}
	
	private int id;
	private String filename;
	private OffsetDateTime createdAt;
	
	public ImageMeta(EntityMemImage entity) {
		id = entity.getId();
		filename = entity.getFilename();
		createdAt = entity.getCreatedAt();
	}
}
