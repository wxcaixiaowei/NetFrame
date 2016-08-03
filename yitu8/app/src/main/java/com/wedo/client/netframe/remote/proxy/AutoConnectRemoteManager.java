package com.wedo.client.netframe.remote.proxy;

import android.util.Log;

import com.wedo.client.netframe.common.MessageCodes;
import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:40:07
 */
public class AutoConnectRemoteManager  extends RemoteManager {

	private RemoteManager remoteManager;
	
	private int maxTryTime = 3;
	
	public AutoConnectRemoteManager(RemoteManager remoteManager, int maxTryTime) {
		super();
		this.remoteManager = remoteManager;
		this.maxTryTime = maxTryTime;
	}

	@Override
	public Request createPostRequest(String target) {
		return remoteManager.createPostRequest(target);
	}

	@Override
	public Request createQueryRequest(String target) {
		return remoteManager.createQueryRequest(target);
	}

	@Override
	public Response execute(Request request) {
		// 注意,这个可能会有一个问题, 就是数据已经发送到服务,
		// 但是服务器响应过程中网络出现问题,那么可能会向服务器发送多次请求
		Response response = null;
		for (int i = 0; i < maxTryTime; ++i) {
			response = remoteManager.execute(request);
			if (response.isSuccess()) {
				return response;
			}
			if (response.getCode() != MessageCodes.CONNECT_FAILED) {
				return response;
			}
			Log.e("AutoConnectRemoteManage" , "网络连接失败, 1秒后重试");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return response;
	}

}
