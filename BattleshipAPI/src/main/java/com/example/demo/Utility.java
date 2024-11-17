package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static String getTimeStamp() {
		String pattern = "yyyy-MM-dd HH:mm:ssZ";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(new Date());
	}
}
