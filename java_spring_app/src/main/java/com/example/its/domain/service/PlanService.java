package com.example.its.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.its.domain.entity.EntityPlan;
import com.example.its.domain.model.PlanForm;
import com.example.its.domain.repository.PlanRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanService {
	private final PlanRepository PlanRepository;

	public Page<EntityPlan> findAll(Pageable pageable) {
		return PlanRepository.findAll(pageable);
	}
	
	@Transactional
	public EntityPlan registerPlan(PlanForm form) {
		EntityPlan entity = new EntityPlan(form);
		return PlanRepository.save(entity);
	}
	
	public PlanForm findById(int id) {
		Optional<EntityPlan> entity = PlanRepository.findById(id);
		
		return new PlanForm(entity.orElse(new EntityPlan()));
	}

	@Transactional
	public EntityPlan updatePlan(PlanForm form) {
		EntityPlan entity = new EntityPlan(form);
		return PlanRepository.save(entity);
	}

	@Transactional
	public void deletePlan(int id) {
		PlanRepository.deleteById(id);
	}
}
