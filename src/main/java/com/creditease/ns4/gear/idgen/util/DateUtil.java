package com.creditease.ns4.gear.idgen.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 */
public class DateUtil {

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	
	public static final String FORMAT_HHMMSS = "HHmmss";
	
	

	public static String date2Str(Date date) {
		return date2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	public static String date2Str(Date date, String format) {
		if (format == null || format.equals("")) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS_SSS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}
	
	//date(8)
	public static String getDate(){
		return date2Str(new Date(),FORMAT_YYYYMMDD); 
	}
	//date(14)
	public static String getDateTime(){
		return date2Str(new Date(),FORMAT_YYYYMMDDHHMMSS); 
	}

	//date(17)
	public static String getDateTimeStr(){
		return date2Str(new Date(),FORMAT_YYYYMMDDHHMMSSSSS);
	}

}
