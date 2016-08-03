package com.wedo.client.netframe.activities.common;


import android.util.Log;

import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;

import java.util.concurrent.Callable;

/**
 * 异步请求
 */

public class ThreadAid implements Callable<Response> {
	
	private  ThreadListener threadListener;

	private Request request;
	
	
	public ThreadAid(  ThreadListener threadListener, Request request) {
		super();
		this.threadListener = threadListener;
		this.request = request;
	}

	@Override
	public Response call() throws Exception {
		Response response = null;
		try {
			RemoteManager remoteManager = RemoteManager.getFullFeatureRemoteManager();
			response = remoteManager.execute(request);
			return response;
		} catch (Exception e) {
			Log.e("ThreadAid", e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			threadListener.onPostExecute(response);
		}
	}


}
