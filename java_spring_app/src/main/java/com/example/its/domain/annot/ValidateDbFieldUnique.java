package com.example.its.domain.annot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Constraint( validatedBy = DbFieldUniqueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateDbFieldUnique {
	String table() default "";
	String field() default "";
	
	String message() default "already exists.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
