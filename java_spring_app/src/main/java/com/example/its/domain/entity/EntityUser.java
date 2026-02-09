package com.example.its.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityUser {
	@Id
	@Column(name = "username", nullable = false, length = 50, unique=true)
	private String username;

	@Column(name = "password", nullable = false, length = 500)
	private String password;

}
