package com.wedo.client.netframe.common;
/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:22:11
 */
public interface ProgressCallback {

	void onSetMaxSize(int maxSize);

	void onProgress(int dealedSize);

	void onFinish();

	void onException(Exception e);
	
}
