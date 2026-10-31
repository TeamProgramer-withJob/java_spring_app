package com.example.its.domain.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.its.domain.entity.EntityNews;

@Repository
public interface NewsRepository extends JpaRepository<EntityNews, Integer> {

	@Query(value="select * from news where (valid_till is null or valid_till >= :now) order by id desc", nativeQuery=true)
	Page<EntityNews> findValidNews(Pageable pageable, @Param("now") OffsetDateTime now);
}
