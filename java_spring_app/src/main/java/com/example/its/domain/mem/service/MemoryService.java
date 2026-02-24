package com.example.its.domain.mem.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;
import com.example.its.domain.mem.entity.EntitySentiment;
import com.example.its.domain.mem.model.MemoryDetail;
import com.example.its.domain.mem.model.MemoryForm;
import com.example.its.domain.mem.repository.MemImageIbatisRepository;
import com.example.its.domain.mem.repository.MemImageRepository;
import com.example.its.domain.mem.repository.MemoryRepository;
import com.example.its.domain.mem.repository.SentimentIbatisRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoryService {
	private final MemoryRepository memoRepository;
	private final MemImageIbatisRepository imageRepository;
	private final SentimentIbatisRepository sentimentRepository;
	
	public List<MemoryForm> findAll() {
		List<EntityMemory> entities = memoRepository.findAll();

		List<MemoryForm> list = new ArrayList<MemoryForm>();
		for (EntityMemory entity : entities) {
			List<EntityMemImage> images = imageRepository.findByMemId(entity.getId());
			
			if (!images.isEmpty()) {
				list.add(new MemoryForm(entity, images.get(0)));
			} else {
				list.add(new MemoryForm(entity, new EntityMemImage()));
			}
		}
		return list;
	}
	
	public MemoryDetail find(int id) {
		EntityMemory entity = memoRepository.findById(id).orElse(new EntityMemory());
		
		if (entity.getId() > 0) {
			List<EntityMemImage> images = imageRepository.findByMemId(entity.getId());
			List<EntitySentiment> thumbups = sentimentRepository.findByMemId(entity.getId());
			
			return new MemoryDetail(entity, images, thumbups);
		}
		
		return new MemoryDetail();
	}

	@Transactional
	public EntityMemory save(MemoryForm form) {
		EntityMemory entity = new EntityMemory();
		entity.setUserId(form.getUserId());
		entity.setTitle(form.getTitle());
		entity.setDetails(form.getDetails());
		entity.setUserId(form.getUserId());

		entity = memoRepository.save(entity);
		
        try {
        	String filename = form.getFile().getOriginalFilename();
        	String contentType = form.getFile().getContentType().toLowerCase();
        	byte[] image = form.getFile().getInputStream().readAllBytes();
        	imageRepository.save(entity.getId(), filename, contentType, image);
        } catch (Exception ex) {
        	
        }
		
		return entity;
	}
	
	public EntityMemImage getImage(int id) {
		return imageRepository.findById(id).orElse(new EntityMemImage());
	}
	
	@Transactional
	public void setThumbup(int userId, int memId, int thumbup) {
		sentimentRepository.save(userId, memId, thumbup);
	}
}
