package com.example.its.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityGenre;
import com.example.its.domain.model.GenreForm;
import com.example.its.domain.model.PulldownIdName;
import com.example.its.domain.repository.GenreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
	private final GenreRepository genreRepository;

	public Page<EntityGenre> findAll(Pageable pageable) {
		return genreRepository.findAll(pageable);
	}
	
	public List<PulldownIdName> findAll() {
		List<EntityGenre> entities = genreRepository.findAll(Sort.by("id").ascending());
		return entities.stream().map(entity -> {
			return new PulldownIdName(String.valueOf(entity.getId()), entity.getTitle());
		}).collect(Collectors.toList());
	}

	@Transactional
	public EntityGenre registerGenre(GenreForm form) {
		EntityGenre entity = new EntityGenre(form);
		return genreRepository.save(entity);
	}
	
	public GenreForm findById(int id) {
		Optional<EntityGenre> entity = genreRepository.findById(id);
		
		return new GenreForm(entity.orElse(new EntityGenre()));
	}

	@Transactional
	public EntityGenre updateGenre(GenreForm form) {
		EntityGenre entity = new EntityGenre(form);
		return genreRepository.save(entity);
	}

	@Transactional
	public void deleteGenre(int id) {
		genreRepository.deleteById(id);
	}
}
