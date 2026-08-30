package com.example.its.domain.mem.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;
import com.example.its.domain.mem.entity.EntitySentiment;
import com.example.its.domain.mem.model.MemoryDetail;
import com.example.its.domain.mem.model.MemoryForm;
import com.example.its.domain.mem.repository.MemImageIbatisRepository;
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
	
	public Page<MemoryForm> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<EntityMemory> entities = memoRepository.findAll(pageable);

		Page<MemoryForm> list = entities.map(entity -> {
			MemoryForm form = new MemoryForm(entity);
			
			List<EntityMemImage> images = imageRepository.findByMemId(entity.getId());
			
			if (!images.isEmpty()) {
				form.setImageId(images.get(0).getId());
				form.setFilename(images.get(0).getFilename());
			}
			
			return form;
		});
//		for (EntityMemory entity : entities) {
//			List<EntityMemImage> images = imageRepository.findByMemId(entity.getId());
//			
//			if (!images.isEmpty()) {
//				list.add(new MemoryForm(entity, images.get(0)));
//			} else {
//				list.add(new MemoryForm(entity, new EntityMemImage()));
//			}
//		}
		return list;
	}
	
	public MemoryDetail find(int id, boolean fetchImags, boolean fetchThumbups) {
		MemoryDetail memoDetail = new MemoryDetail(memoRepository.findById(id).orElse(new EntityMemory()));
		
		if (memoDetail.getId() > 0) {
			if (fetchImags) {
				memoDetail.addImages(imageRepository.findByMemId(memoDetail.getId()));
			}
			if (fetchThumbups) {
				memoDetail.addThumbups(sentimentRepository.findByMemId(memoDetail.getId()));
			}
		}

		return memoDetail;
	}

	public MemoryForm find(int id) {
		MemoryForm formData = new MemoryForm(memoRepository.findById(id).orElse(new EntityMemory()));
		return formData;
	}

	@Transactional
	public EntityMemory save(MemoryForm form) {
		EntityMemory entity = null;

		if (form.getAct().equals("image")) {
			entity = new EntityMemory();
			entity.setId(form.getId());
		} else {
			if (form.getId() > 0) {
				Optional<EntityMemory> tE = memoRepository.findById(form.getId());
				if (tE != null) {
					entity = tE.get();
					entity.setLastUpdated(OffsetDateTime.now());
				}
			} else {
				entity = new EntityMemory();
				entity.setUserId(form.getUserId());
			}
			entity.setTitle(form.getTitle());
			entity.setDetails(form.getDetails());
			entity = memoRepository.save(entity);
		}
		
		if (!form.getFile().isEmpty()) {
	        try {
	        	String filename = form.getFile().getOriginalFilename();
	        	String contentType = form.getFile().getContentType().toLowerCase();
	        	byte[] image = form.getFile().getInputStream().readAllBytes();
	        	imageRepository.save(entity.getId(), filename, contentType, image);
	        } catch (Exception ex) {
	        	
	        }
		}
		
		return entity;
	}
	
	public EntityMemImage getImage(int id) {
		return imageRepository.findById(id).orElse(new EntityMemImage());
	}
	
	@Transactional
	public void setThumbup(int userId, int memId, int thumbup) {
		Optional<EntitySentiment> thumbupE = sentimentRepository.findThumbUpByUserAndMemId(userId, memId);
		if (thumbupE.isEmpty()) {
			sentimentRepository.save(userId, memId, thumbup);
		}
	}

	@Transactional
	public void delete(int id) {
		memoRepository.deleteById(id);
	}

	@Transactional
	public int deleteImage(int id) {
		Optional<EntityMemImage> image = imageRepository.findById(id);
		
		if (image != null) {
			imageRepository.delete(id);
			return image.get().getMemId();
		} else {
			return 0;
		}
	}
}
