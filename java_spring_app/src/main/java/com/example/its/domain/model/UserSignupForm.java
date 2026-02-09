package com.example.its.domain.model;

import com.example.its.domain.annot.PasswordMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class UserSignupForm {
	@NotBlank(message="必須入力項目です。")
	@Size(max=128, message="128文字以内で入力してください。")
    private String username;

	@NotBlank(message="必須入力項目です。")
	@Size(max=20, message="パスワードは20文字以内で入力してください。")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,}$",
	         message = "パスワードは3文字以上、小文字、大文字、数字と特殊文字を少なくとも1文字以上を含むこと。")
    private String password;

	private String password2;
}
