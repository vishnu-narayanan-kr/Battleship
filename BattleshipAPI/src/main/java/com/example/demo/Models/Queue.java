package com.example.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "queue")
public class Queue {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer qId;
	 @Column(columnDefinition = "NVARCHAR(50)")
	 private String username;
	 @Column(columnDefinition = "VARCHAR(30)")
	 private String queuedTime;
}
