package com.example.its.domain.inquiry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class InquiryService {
    private final InquiryMapper mapper;

    public InquiryService(InquiryMapper mapper) {
        this.mapper = mapper;
    }

    public Inquiry save(Inquiry inquiry) {
        mapper.insert(inquiry); // useGeneratedKeys で inquiry.id に採番が入る
        return inquiry;
    }

    public List<Inquiry> findAll() {
        return mapper.findAll();
    }
}
