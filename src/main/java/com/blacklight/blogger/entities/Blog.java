package com.blacklight.blogger.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Builder
public class Blog {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private Long id;

	@NotNull
	@Column(length=50)
	private String title;

	@NotNull
	@Column(length = 10000)
	private String text;

	@NotNull
	@Column(length=10)
	private String date;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
}
