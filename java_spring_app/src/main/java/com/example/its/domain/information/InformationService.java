package com.example.its.domain.information;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    public List<InformationEntity> findAll() {
        return informationRepository.findAll();
    }

    public InformationEntity findById(long id) {
        return informationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Information not found. id=" + id));
    }

    public void create(String informationTitle, String informationDetail) {
        informationRepository.insert(informationTitle, informationDetail);
    }

    public void update(long id, String informationTitle, String informationDetail) {
        informationRepository.update(id, informationTitle, informationDetail);
    }

    public void delete(long id) {
        informationRepository.delete(id);
    }
}
