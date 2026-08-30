package com.example.its.domain.annot;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleMatchesValidator implements ConstraintValidator<RoleMatches, String> {
	private String[] roles;

	@Override
	public void initialize(RoleMatches constraintContext) {
		this.roles = constraintContext.roles();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean valueExist = false;

		if (value == null) {
			valueExist = true;
		} else {
			for (String role : roles) {
				if (role.equals(value.toUpperCase())) {
					return true;
				}
			}
		}
		return valueExist;
	}
}
