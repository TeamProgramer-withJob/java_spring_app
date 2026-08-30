package com.example.its.domain.mem.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;
import com.example.its.domain.mem.entity.EntitySentiment;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemoryDetail {
	public record Sentiment(int id, int userId) {}
	
	private int id;
	private String title;
	private String details;
	private OffsetDateTime createdAt;
	private OffsetDateTime lastUpdated;
	
	private List<Integer> imgIds = new ArrayList<Integer>();
	private List<Sentiment> thumbups = new ArrayList<Sentiment>();
	
	public MemoryDetail(EntityMemory entity) {
		id = entity.getId();
		title = entity.getTitle();
		details = entity.getDetails();
		createdAt = entity.getCreatedAt();
		lastUpdated = entity.getLastUpdated();
	}

	public void addImages(List<EntityMemImage> images) {
		for (EntityMemImage image: images) {
			this.imgIds.add(image.getId());
		}
	}
	public void addThumbups(List<EntitySentiment> thumbups) {
		for (EntitySentiment thumbup: thumbups) {
			Sentiment tLike = new Sentiment(thumbup.getId(), thumbup.getUserId());
			this.thumbups.add(tLike);
		}
	}
}
