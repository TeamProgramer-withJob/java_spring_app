package com.example.its.domain.annot;

import com.example.its.domain.model.UserSignupForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		UserSignupForm form = (UserSignupForm)value;
		boolean matched = form.getPassword() != null && form.getPassword().equals(form.getPassword2());
		if (!matched) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
			       .addPropertyNode("password2")
			       .addConstraintViolation();
		}

		return matched;
	}

}
