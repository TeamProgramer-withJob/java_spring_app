package com.example.its.domain.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.issue.entity.EntityIssue;

@Repository
public interface Issue2Repository extends JpaRepository<EntityIssue, Long> {

}
