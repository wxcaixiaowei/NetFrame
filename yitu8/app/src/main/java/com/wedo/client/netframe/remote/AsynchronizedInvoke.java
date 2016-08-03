
package com.wedo.client.netframe.remote;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:01:45
 */
public class AsynchronizedInvoke {
private ExecutorService executorService;
	
	public void init() {
		executorService = Executors.newCachedThreadPool();

	}
	

	
	public <V> Future<V> invoke(Callable<V> callable) {
		return executorService.submit(callable);
	}
	
	public void call(Runnable runnable) {
		executorService.submit(runnable);
	}
	


	public void cleanup() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}
	
	
}

