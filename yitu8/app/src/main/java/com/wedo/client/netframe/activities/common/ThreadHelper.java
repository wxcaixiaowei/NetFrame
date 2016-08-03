package com.wedo.client.netframe.activities.common;


import android.app.ProgressDialog;
import android.util.Log;

import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;

import java.util.concurrent.Callable;


/**
 * @author wxc
 * 同步请求，自带dialog做缓冲
 *
 */
public class ThreadHelper implements Callable<Response> {
	
	private ProgressDialog progressDialog;

	private Request request;
	
	
	public ThreadHelper(ProgressDialog progressDialog, Request request) {
		super();
		this.progressDialog = progressDialog;
		this.request = request;
	}

	@Override
	public Response call() throws Exception {
		try {
			RemoteManager remoteManager = RemoteManager.getFullFeatureRemoteManager();
			Response response = remoteManager.execute(request);
//			Log.d("response", String.valueOf(response));
			progressDialog.setProgress(100);
			Thread.sleep(200);
			return response;
		} catch (Exception e) {
			Log.e("ThreadHelper", e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			progressDialog.dismiss();
		}
	}


}