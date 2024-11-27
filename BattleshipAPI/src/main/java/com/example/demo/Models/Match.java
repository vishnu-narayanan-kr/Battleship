package com.example.demo.Models;

import com.example.demo.Utility;

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
 @Column(columnDefinition = "VARCHAR(30)")
 private String lastMovedAt;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String currentPlayer;
 @Column(columnDefinition = "VARCHAR(100)")
 private String p1Grid;
 @Column(columnDefinition = "VARCHAR(100)")
 private String p2Grid;
 private boolean isActive;
 @Column(columnDefinition = "NVARCHAR(50)")
 private String winner;
 
 public Match getMatchWithHiddenGrid(String username) {
	Match match = new Match(
				mId,
				p1,
				p2,
				startTime,
				lastMovedAt,
				currentPlayer,
				p1Grid,
				p2Grid,
				isActive,
				winner
			);
	
	if(p1.equals(username)) {
		match.setP2Grid(Utility.getHiddenGrid(p2Grid));
	} else {
		match.setP1Grid(Utility.getHiddenGrid(p1Grid));
	}
	 
	 return match;
 }
}