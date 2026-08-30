package com.example.its.domain.model;

import java.time.OffsetDateTime;

import com.example.its.domain.annot.OnCreate;
import com.example.its.domain.annot.OnEdit;
import com.example.its.domain.entity.EntityInquiry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InquiryForm {
	private int id;
	
	@NotBlank(groups= {OnCreate.class}, message = "必須項目です。")
	@Size(max = 128)
	private String name;
	
	@NotBlank(groups= {OnCreate.class}, message = "必須項目です。")
	@Size(max = 256)
	private String email;

	@NotBlank(groups= {OnCreate.class, OnEdit.class}, message = "必須項目です。")
	@Size(max = 512)
	private String subject;
	
	@NotBlank(groups= {OnCreate.class, OnEdit.class}, message = "必須項目です。")
	@Size(max = 1024)
	private String message;
	
	private OffsetDateTime createdAt;

	public InquiryForm(EntityInquiry entity) {
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		subject = entity.getSubject();
		message = entity.getMessage();
		createdAt = entity.getCreatedAt();
	}
}
