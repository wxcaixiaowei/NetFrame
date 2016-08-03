package com.wedo.client.netframe.common;

import android.util.DisplayMetrics;

import com.wedo.client.netframe.androidext.ChenApplication;


public class MobileUseInfo {

	private String mobile;

	private String netWork;

	private int width;
	 
	private int height;
	 

	private static MobileUseInfo mobileConfig = new MobileUseInfo();

	private ChenApplication cardApplication;

	private MobileUseInfo() {

	}

	public static MobileUseInfo getMobileUseInfo() {
		return mobileConfig;
	}

	public void init( ChenApplication cardApplication) {
		this.cardApplication = cardApplication;
		this.mobile = android.os.Build.MODEL + "/" + android.os.Build.VERSION.SDK + "/" + android.os.Build.VERSION.RELEASE;
		this.netWork = AndroidUtil.getNetwork(cardApplication);
		 DisplayMetrics dm =  this.cardApplication.getResources().getDisplayMetrics();
		 width = dm.widthPixels;
		 height = dm.heightPixels;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNetWork() {
		return netWork;
	}

	public void setNetWork(String netWork) {
		this.netWork = netWork;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}