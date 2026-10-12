package com.example.its.domain.model;

import com.example.its.domain.annot.OnCreate;
import com.example.its.domain.annot.OnEdit;
import com.example.its.domain.annot.PasswordMatches;
import com.example.its.domain.annot.ValidateDbFieldUnique;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches(groups = {OnCreate.class, OnEdit.class})
public class UserSignupForm {
	@NotBlank(groups={OnCreate.class}, message="必須入力項目です。")
	@Size(groups={OnCreate.class}, max=128, message="128文字以内で入力してください。")
    private String name;

	@NotBlank(groups={OnCreate.class, OnEdit.class}, message="必須入力項目です。")
	@Size(groups={OnCreate.class, OnEdit.class}, max=20, message="パスワードは20文字以内で入力してください。")
	@Pattern(groups={OnCreate.class, OnEdit.class}, regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,}$",
	         message = "パスワードは3文字以上、小文字、大文字、数字と特殊文字を少なくとも1文字以上を含むこと。")
    private String password;

	private String password2;
	
	@NotBlank(groups={OnCreate.class}, message="必須入力項目です。")
	@Size(groups={OnCreate.class}, max=256, message="256文字以内で入力してください。")
	@ValidateDbFieldUnique(groups={OnCreate.class}, table = "users", field = "email", message = "入力した電子メールはすでに利用されています。")
    private String email;
}
