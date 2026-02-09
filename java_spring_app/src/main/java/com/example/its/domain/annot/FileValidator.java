package com.example.its.domain.annot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidateFile, MultipartFile> {
	private long maxFileSize;
	private List<String> allowedTypes = new ArrayList<String>();
	private String[] messages;
	
	@Override
	public void initialize(ValidateFile constraintAnnotation) {
		this.maxFileSize = constraintAnnotation.maxSize();
		
		for (String message : constraintAnnotation.types()) {
			this.allowedTypes.add(message);
		}
		this.messages = constraintAnnotation.messages();
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null || file.isEmpty()) return false;
		
		if (file.getSize() > maxFileSize && messages.length >= 1) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messages[0])
			       .addConstraintViolation();
			return false;
		} else if (!allowedTypes.contains(file.getContentType()) && messages.length >= 2) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messages[1])
			       .addConstraintViolation();
			return false;
		}

		return true;
	}

}
