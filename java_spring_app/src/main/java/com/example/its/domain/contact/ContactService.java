package com.example.its.domain.contact;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<ContactEntity> findAll() {
        return contactRepository.findAll();
    }

    public ContactEntity findById(long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found. id=" + id));
    }

    public void create(String name, String email, String subject, String message) {
        contactRepository.insert(name, email, subject, message);
    }

    public void delete(long id) {
        contactRepository.delete(id);
    }
}
