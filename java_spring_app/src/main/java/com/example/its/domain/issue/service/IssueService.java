package com.example.its.domain.issue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.its.domain.issue.model.Issue2Form;
import com.example.its.domain.issue.repository.IssueRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public List<Issue2Form> findAll() {
        return issueRepository.findAll();
    }

    @Transactional
    public void create(String summary, String description, String fullpath, int mediaType) {
        issueRepository.insert(summary, description, fullpath, mediaType);
    }

    public Issue2Form findById(long issueId) {
        return issueRepository.findById(issueId);
    }
}
