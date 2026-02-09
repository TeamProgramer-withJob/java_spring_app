package com.example.its.domain.annot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = FileValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateFile {
	long maxSize() default 2*1024*1024; // default is 2 MB
	String[] types() default{ "image/png", "image/jpeg" };
	// check file size, and file format, corresponding messages are set to [0], [1]
	String[] messages() default { "File too big.", "File format is not supported." };

	String message() default "ファイルを選択してください。";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
