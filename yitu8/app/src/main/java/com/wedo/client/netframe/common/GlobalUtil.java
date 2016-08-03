package com.wedo.client.netframe.common;


import com.wedo.client.netframe.androidext.ChenApplication;

/**
 * @author wxc
 *
 * @date 2013-5-30 上午11:16:29
 */
public class GlobalUtil {
	
	private static ChenApplication shenApplication;

	private static float density = 1.0f;
	
	public static  ChenApplication getChildayApplication() {
		return shenApplication;
	}
	
	public static void init( ChenApplication childayApplication) {
		GlobalUtil.shenApplication = childayApplication;
		density = childayApplication.getResources().getDisplayMetrics().density;
	}
	
	public static int pixelToDip(int pixel) {
		return (int)(pixel / density);
	}
	
	public static int dipToPixel(int dip) {
		return (int)(dip * density);
	}
	
}
