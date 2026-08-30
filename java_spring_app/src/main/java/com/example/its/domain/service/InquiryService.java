package com.example.its.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityInquiry;
import com.example.its.domain.model.InquiryForm;
import com.example.its.domain.repository.InquiryRepoMapper;
import com.example.its.domain.repository.InquiryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {
	private final InquiryRepository inquiryRepository;
	private final InquiryRepoMapper inquiryMapper;

//	@Transactional
//	public void save(UserSignupForm user) {
//		String encryptedPwd = passwordEncoder.encode(user.getPassword());
//		userRepository.save(user.getName(), encryptedPwd, user.getEmail(), "USER");
//	}
	
	public Page<EntityInquiry> findAll(Pageable pageable) {
		int size = pageable.getPageSize();
		int offset = pageable.getPageNumber() * size;
		List<EntityInquiry> entities = inquiryMapper.findAllWithUser(size, offset);
		return new PageImpl<EntityInquiry>(entities, pageable, inquiryMapper.totalCount());
		
//		return inquiryRepository.findAll(pageable);
	}
	
	@Transactional
	public EntityInquiry registerInquiry(InquiryForm form) {
		EntityInquiry entity = new EntityInquiry(form);
		return inquiryRepository.save(entity);
	}
	
	public InquiryForm findById(int id) {
		Optional<EntityInquiry> entity = inquiryRepository.findById(id);
		
		return new InquiryForm(entity.orElse(new EntityInquiry()));
	}

	@Transactional
	public EntityInquiry updateInquiry(InquiryForm form) {
		EntityInquiry entity = new EntityInquiry(form);
		return inquiryRepository.save(entity);
	}
	@Transactional
	public void deleteInquiry(int id) {
		inquiryRepository.deleteById(id);
	}
}
