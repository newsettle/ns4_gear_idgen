package com.creditease.ns4.gear.idgen.util;

import java.util.Random;


public class SeqNoUtil {

	
	public static String getSeqNo(int strLength){ 
	
		return getFixLenthString(strLength); 
	}
	
	
	private static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    String fixLenthString = String.valueOf(pross);  
	  
	    return fixLenthString.substring(1, strLength + 1);  
	}  
	
	
}
