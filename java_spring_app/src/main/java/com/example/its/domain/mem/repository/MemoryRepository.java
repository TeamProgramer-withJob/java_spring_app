package com.example.its.domain.mem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.mem.entity.EntityMemory;

@Repository
public interface MemoryRepository extends JpaRepository<EntityMemory, Integer> {

}
