package com.example.its.domain.mem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;

@Repository
public interface MemImageRepository extends JpaRepository<EntityMemImage, Integer> {
	public List<EntityMemImage> findByMemId(int memId);
}
