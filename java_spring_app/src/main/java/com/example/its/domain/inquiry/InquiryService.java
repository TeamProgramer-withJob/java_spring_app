package com.example.its.domain.inquiry;

import org.springframework.stereotype.Service;

@Service
public class InquiryService {
    private final InquiryRepository repository;

    public InquiryService(InquiryRepository repository) {
        this.repository = repository;
    }

    public void save(Inquiry inquiry) {
        repository.save(inquiry);
    }
}
