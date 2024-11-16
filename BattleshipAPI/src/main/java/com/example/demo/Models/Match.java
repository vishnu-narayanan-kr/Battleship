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
@Table(name = "matches")
public class Match {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer mId;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String p1;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String p2;
 @Column(columnDefinition = "VARCHAR(30)")
 private String startTime;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String currentPlayer;
 @Column(columnDefinition = "VARCHAR(100)")
 private String p1Grid;
 @Column(columnDefinition = "VARCHAR(100)")
 private String p2Grid;
 private boolean isActive;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String winner;
}