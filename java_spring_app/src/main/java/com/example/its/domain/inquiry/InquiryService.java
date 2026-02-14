package com.example.its.domain.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    public List<InquiryEntity> findAll() {
        return inquiryRepository.findAll();
    }

    @Transactional
    public void create(String name, String email, String subject, String message) {
        InquiryEntity inquiry = new InquiryEntity();
        inquiry.setName(name);
        inquiry.setEmail(email);
        inquiry.setSubject(subject);
        inquiry.setMessage(message);
        inquiryRepository.insert(inquiry);
    }

    public InquiryEntity findById(long inquiryId) {
        return inquiryRepository.findById(inquiryId);
    }
}
