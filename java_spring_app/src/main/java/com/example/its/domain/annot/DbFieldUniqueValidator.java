package com.example.its.domain.annot;

import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DbFieldUniqueValidator implements ConstraintValidator<ValidateDbFieldUnique, String> {
	private final JdbcTemplate db;
	private String tableName;
	private String fieldName;

	@Override
	public void initialize(ValidateDbFieldUnique constraintAnnotation) {
		this.tableName = constraintAnnotation.table();
		this.fieldName = constraintAnnotation.field();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) return true;
		
		String sql = String.format("select count(*) from %s where %s=?", tableName, fieldName);
		Integer count = db.queryForObject(sql, Integer.class, value);
		return count != null && count == 0;
	}

}
