package com.example.its.domain.model;

import java.time.OffsetDateTime;

import com.example.its.domain.annot.PasswordMatches;
import com.example.its.domain.annot.RoleMatches;
import com.example.its.domain.entity.EntityUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@PasswordMatches
public class UserEditForm {
	private int id;
	
	@NotBlank(message="必須入力項目です。")
	@Size(max=128, message="128文字以内で入力してください。")
    private String name;

	@Size(max=20, message="パスワードは20文字以内で入力してください。")
	@Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,})?$",
	         message = "パスワードは3文字以上、小文字、大文字、数字と特殊文字を少なくとも1文字以上を含むこと。")
    private String password;

	private String password2;
	
    private String email;

    @RoleMatches
    private String role;

    private boolean valid;
    private boolean forcePwdChange;
    private String subscriptionId;
    
    private int planId;
	private OffsetDateTime createdAt;
    
    public UserEditForm(EntityUser entity) {
    	id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		role = entity.getRole();
		valid = entity.isValid();
		forcePwdChange = entity.isForcePwdChange();
		subscriptionId = entity.getSubscriptionId();
		planId = entity.getPlanId();
		createdAt = entity.getCreatedAt();
	}
}
