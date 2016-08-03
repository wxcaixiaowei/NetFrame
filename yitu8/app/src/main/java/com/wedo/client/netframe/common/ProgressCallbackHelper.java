package com.wedo.client.netframe.common;
/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:22:54
 */
public class ProgressCallbackHelper {
private ProgressCallback progressCallback;
	
	private int count;
	
	private int process;

	public ProgressCallbackHelper(int count, ProgressCallback progressCallback) {
		super();
		this.count = count;
		this.progressCallback = progressCallback;
	}

	public ProgressCallback getProgressCallback() {
		return progressCallback;
	}

	public void setProgressCallback(ProgressCallback progressCallback) {
		this.progressCallback = progressCallback;
	}

	public int getProcess() {
		return process;
	}
	
	public void addProcess(int size) {
		process += size;
		progressCallback.onProgress(process);
	}

	public void setProcess(int process) {
		this.process = process;
	}
	
	public synchronized void releaseCount() {
		count--;
	}
	
	public boolean isFinish() {
		return count <= 0;
	}

}

