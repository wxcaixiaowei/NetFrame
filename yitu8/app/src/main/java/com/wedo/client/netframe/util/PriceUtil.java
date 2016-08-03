package com.wedo.client.netframe.util;

public class PriceUtil {
	
	public static String showPrice(int value) {
		double d = Double.valueOf(value);
		return d / 100.d + "元";
	}
	
	public static String showPriceNonUnit(int value) {
		double d = Double.valueOf(value);
		return d / 100.d + "";
	}
	
	
	public static String showPriceYang(int value) {
		double d = Double.valueOf(value);
		return "¥" + d / 100.d;
	}

}
