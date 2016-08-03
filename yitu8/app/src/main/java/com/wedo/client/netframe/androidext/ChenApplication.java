package com.wedo.client.netframe.androidext;

import android.app.Application;

import com.wedo.client.netframe.remote.AsynchronizedInvoke;
import com.wedo.client.netframe.remote.RemoteManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 2013/7/29/10:23:45
 * author wxc
 * </p>
 */
public class ChenApplication extends Application {

private AsynchronizedInvoke asynchronizedInvoke;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {

		RemoteManager.init(this);
		asynchronizedInvoke = new AsynchronizedInvoke();
		asynchronizedInvoke.init();
			
	}

	@Override
	public void onTerminate() {
		asynchronizedInvoke.cleanup();
		super.onTerminate();
	}

	
	public <V> Future<V> asyInvoke(Callable<V> callable) { 
		return asynchronizedInvoke.invoke(callable);
	}

	public void asyCall(Runnable runnable) {
		asynchronizedInvoke.call(runnable);
	}
	


}
