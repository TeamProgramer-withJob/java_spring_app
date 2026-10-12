package com.example.its.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.entity.EntityGenre;

@Repository
public interface GenreRepository extends JpaRepository<EntityGenre, Integer> {

}
