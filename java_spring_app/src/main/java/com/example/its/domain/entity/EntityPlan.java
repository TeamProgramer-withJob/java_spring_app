package com.example.its.domain.entity;

import java.time.OffsetDateTime;

import com.example.its.domain.model.PlanForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "plans")
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false, length = 128)
	private String name;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "remark", nullable = true, length = 512)
	private String remark;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createdAt;
	
	public EntityPlan(PlanForm form) {
		id = form.getId();
		name = form.getName();
		price = form.getPrice();
		remark = form.getRemark();
		if (form.getCreatedAt() != null) {
			createdAt = form.getCreatedAt();
		}
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now();
		}
	}
}
