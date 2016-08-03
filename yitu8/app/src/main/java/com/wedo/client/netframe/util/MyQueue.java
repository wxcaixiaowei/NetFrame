package com.wedo.client.netframe.util;
import java.util.LinkedList;

/**
 * @author wxc
 * 
 * @date 2013-5-29 下午11:04:03
 */
public class MyQueue<T> {
	
	private LinkedList<T> queue = new LinkedList<T>();

	public synchronized void add(T t) {
		queue.add(t);
		notifyAll();
	}

	public synchronized T take() {
		while (queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return queue.removeFirst();
	}
}