package com.example.its.domain.annot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RoleMatchesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleMatches {
	String[] roles() default {"ADMIN", "USER"};
	String message() default "入力したロール名は存在しません。";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
