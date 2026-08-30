package com.example.its.domain.entity;

import java.time.OffsetDateTime;
import java.util.List;

import com.example.its.domain.model.InquiryForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "inquiries")
@ToString
@Data
@RequiredArgsConstructor
public class EntityInquiry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false, length = 128)
	private String name;
	
	@Column(name = "email", nullable = false, length = 256)
	private String email;

	@Column(name = "subject", nullable = false, length = 128)
	private String subject;

	@Column(name = "message", nullable = false, length = 1024)
	private String message;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createdAt;
	
	@Transient
	private boolean EmailExist = false;
	
//	@ManyToOne(fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name="email", referencedColumnName = "email", nullable = true)
//	private EntityUser userE;
	
	public EntityInquiry(InquiryForm form) {
		name = form.getName();
//		userE = new EntityUser();
//		userE.setEmail(form.getEmail());
		email = form.getEmail();
		subject = form.getSubject();
		message = form.getMessage();
	}
	
	public EntityInquiry(Object rec) {
		
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now();
		}
	}
}
