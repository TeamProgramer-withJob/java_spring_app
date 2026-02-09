package com.example.its.domain.issue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "issues")
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityIssue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "summary", nullable = false, length = 256)
	private String summary;

	@Column(name = "description", nullable = false, length = 256)
	private String description;

	@Column(name = "fullpath", nullable = true, length = 512)
	private String fullpath;

	@Column(name = "media_type")
	private int mediaType = -1;
}
