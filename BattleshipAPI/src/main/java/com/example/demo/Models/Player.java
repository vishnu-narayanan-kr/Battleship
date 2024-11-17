package com.example.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {
	@Id
	@Column(columnDefinition = "NVARCHAR(50)")
	private String username;
	@Column(columnDefinition = "VARCHAR(30)")
	private String lastSeen;
	@Column(columnDefinition = "VARCHAR(20)")
	private String language;
}
