package com.creditease.ns4.gear.idgen.util;

import java.util.Arrays;

public class SpecNumUtil {

	private static char[] attr = new char[] { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };

	public static String increase(String value) {
		char[] arrays = value.toCharArray();
		char[] newVal = new char[arrays.length + 1];
		newVal[0] = attr[0];
		for (int index = 1; index <= arrays.length; index++) {
			newVal[index] = arrays[index - 1];
		}
		char[] rs = increase(newVal);
		for (int index = 0; index < rs.length; index++) {
			if (rs[index] != attr[0]) {
				break;
			}
			rs[index] = ' ';
		}
		return new String(rs).trim();
	}

	private static char[] increase(char val[]) {
		return increase(val, val.length - 1);
	}

	private static char[] increase(char val[], int index) {
		int len = val.length;
		if (len <= index) {
			return val;
		}
		char srcChar = val[index];
		char destChar = increase(srcChar, 1);
		val[index] = destChar;
		if (0 == index) {
			return val;
		}
		if (destChar == attr[0]) {
			increase(val, index - 1);
			return val;
		}
		return val;
	}

	private static char increase(char val, int rate) {
		int index = Arrays.binarySearch(attr, val);
		return attr[(index + rate) % attr.length];
	}

	public static void main(String[] args) {
		String str = "0";
		int num = 0; 
		long start = System.currentTimeMillis(); 
		for (int index = 0; index < 1000000; index++) {
			str = increase(str);
//			num ++; 
		}
		
		long end = System.currentTimeMillis(); 
		System.out.println(num);
		System.out.println("time:" + (end-start));
	}

}
