package com.example.its.domain.model;

import java.time.OffsetDateTime;

import com.example.its.domain.entity.EntityPlan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlanForm {
	private int id;
	
	@NotBlank(message = "必須項目です。")
	@Size(max = 128)
	private String name;
	
	@PositiveOrZero(message = "値は0または0以上の数値を入力してください。")
	private int price;
	
	@Size(max = 512, message = "備考欄は最大512文字まで入力してください。")
	private String remark;

	private OffsetDateTime createdAt;

	public PlanForm(EntityPlan entity) {
		id = entity.getId();
		name = entity.getName();
		price = entity.getPrice();
		remark = entity.getRemark();
		createdAt = entity.getCreatedAt();
	}
}
