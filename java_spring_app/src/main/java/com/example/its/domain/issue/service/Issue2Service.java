package com.example.its.domain.issue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.its.domain.issue.entity.EntityIssue;
import com.example.its.domain.issue.repository.Issue2Repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Issue2Service {
	private final Issue2Repository issueService;
	
	public List<EntityIssue> findAll2() {
		return issueService.findAll();
	}
}
