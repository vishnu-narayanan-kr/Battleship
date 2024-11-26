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
				grid[i / 10][i % 10] = Integer.parseInt(String.valueOf(gridString.charAt(i)));
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

	public static String getUpdatedGridString(int x, int y, int[][] grid) throws Exception {
		if ((((10 * x) + y) < 0) || (99 < ((10 * x) + y))) { 
			throw new Exception("Invalid move");
		}
		
		int currentCellValue = grid[x][y];
		
		if((currentCellValue < 3 || 4 < currentCellValue)) {
			throw new Exception("Invalid move");
		}
		
		grid[x][y] -= 2;
		
		return getGridString(grid);
	}
	
	public static boolean isWinner(int[][] grid) {
		for(int i = 0; i < 100; i++) {
			if(grid[i / 10][i % 10] == 4) {
				return false;
			}
		}
		
		return true;
	}
}
