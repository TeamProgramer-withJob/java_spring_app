package com.example.its.domain.inquiry;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", updatable = false, insertable = false)
    private java.sql.Timestamp createdAt;
}
