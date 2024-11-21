package com.example.demo.Models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SocketMessage {
	private String username;
	private String message;
	
	private int x;
	private int y;
}
