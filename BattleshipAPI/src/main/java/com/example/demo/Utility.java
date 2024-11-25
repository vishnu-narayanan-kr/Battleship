package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static String getTimeStamp() {
		String pattern = "yyyy-MM-dd HH:mm:ssZ";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(new Date());
	}
	
	public static int[][] getGridArray(String gridString) {
		int[][] grid = new int[10][10];
		
		try {
			
			for(int i = 0; i < 100; i++) {
				grid[i / 10][i % 10] = gridString.charAt(i);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Grid convertion error!");
		}
		
		return grid;
	}
	
	public static String getGridString(int[][] grid) {
		String gridString = "";
		
		try {
			
			for(int i = 0; i < 100; i++) {
				gridString += grid[i / 10][i % 10];
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Grid convertion error!");
		}
		
		return gridString;
	}
}
