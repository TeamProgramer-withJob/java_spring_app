package com.example.its.domain.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityNews;
import com.example.its.domain.model.NewsForm;
import com.example.its.domain.repository.NewsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	private final NewsRepository newsRepository;

	public Page<NewsForm> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		
		Page<EntityNews> entities = newsRepository.findValidNews(pageable, OffsetDateTime.now());
		Page<NewsForm> forms = entities.map(entity -> {
			return new NewsForm(entity);
		});
		
		return forms;
	}
	
	@Transactional
	public EntityNews registerNews(NewsForm form) {
		EntityNews entity = new EntityNews(form);
		return newsRepository.save(entity);
	}
	
	public NewsForm findById(int id) {
		Optional<EntityNews> entity = newsRepository.findById(id);
		
		return new NewsForm(entity.orElse(new EntityNews()));
	}

	@Transactional
	public void deleteNews(int id) {
		newsRepository.deleteById(id);
	}
}
