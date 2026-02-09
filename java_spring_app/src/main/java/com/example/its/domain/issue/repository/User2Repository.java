package com.example.its.domain.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.its.domain.entity.EntityUser;

@Repository
public interface User2Repository extends JpaRepository<EntityUser, String> {

}
