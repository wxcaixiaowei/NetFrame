package com.wedo.client.netframe.remote;
/**
 * @author wxc
 * 
 * @date 2013-5-29 下午11:20:05
 */
public class BinaryItem {

	public BinaryItem() {
		super();
	}

	public BinaryItem(String name, byte[] data) {
		super();
		this.name = name;
		this.data = data;
	}

	public String name;

	public byte[] data;

}