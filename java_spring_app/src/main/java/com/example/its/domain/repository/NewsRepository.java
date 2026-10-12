package com.example.its.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.entity.EntityNews;

@Repository
public interface NewsRepository extends JpaRepository<EntityNews, Integer> {

}
