package com.example.its.domain.annot;

import com.example.its.domain.model.UserEditForm;
import com.example.its.domain.model.UserSignupForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String pwd1 = null;
		String pwd2 = null;
		if (value instanceof UserEditForm) {
			UserEditForm form = (UserEditForm)value;
			pwd1 = form.getPassword();
			pwd2 = form.getPassword2();
		} else {
			UserSignupForm form = (UserSignupForm)value;
			pwd1 = form.getPassword();
			pwd2 = form.getPassword2();
		}

		boolean matched = pwd1 != null && pwd1.equals(pwd2);
		if (!matched) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
			       .addPropertyNode("password2")
			       .addConstraintViolation();
		}

		return matched;
	}

}
