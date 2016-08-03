package com.wedo.client.netframe.remote;


import com.wedo.client.netframe.androidext.ChenApplication;
import com.wedo.client.netframe.remote.http.HttpRemoteManager;
import com.wedo.client.netframe.remote.proxy.AutoConnectRemoteManager;
import com.wedo.client.netframe.remote.proxy.CheckNetworkStateRemoteManager;
import com.wedo.client.netframe.remote.proxy.SecurityRemoteManager;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:18:35
 */
public abstract class RemoteManager {

public abstract Request createQueryRequest(String target);
	
	public abstract Request createPostRequest(String target);
	
	public abstract Response execute(Request request);
	
	protected static ChenApplication shenApplication;
	
	private static RemoteManager defaultRemoteManager = new HttpRemoteManager();
	
	public static void init(ChenApplication shenApplication) {
		RemoteManager.shenApplication = shenApplication;
	}
	
	/**
	 * 原生的RemoteManager，没有做过任何处理， 一般不推荐使用
	 * @return
	 */
	public static RemoteManager getRawRemoteManager() {
		return defaultRemoteManager;
	}
	
	/**
	 * 后台触发的不用登录建议使用这个
	 * 
	 * 带安全签名的RemoteManager
	 * @return
	 */
	public static RemoteManager getSecurityRemoteManager() {
		return new CheckNetworkStateRemoteManager(new SecurityRemoteManager(defaultRemoteManager));
	}
	
	
	public static RemoteManager getAutoLoginSecurityRemoteManager() {
		return new CheckNetworkStateRemoteManager(new SecurityRemoteManager(defaultRemoteManager));
	}
	
	/**
	 * 新增数据的时候建议用这个，这个不会进行重试
	 * 
	 * 1、带安全签名
	 * 4、检查网络状态
	 * @return
	 */
	public static RemoteManager getPostOnceRemoteManager() {
		return new CheckNetworkStateRemoteManager(new SecurityRemoteManager(defaultRemoteManager));
	}
	
	/**
	 * 用户主动触发的一般建议使用这个
	 * 
	 * 1、带安全签名
	 * 2、自动重连（3次）
	 * 3、自动登录
	 * 4、检查网络状态
	 * @return
	 */
	public static RemoteManager getFullFeatureRemoteManager() {
		return new CheckNetworkStateRemoteManager(new AutoConnectRemoteManager(new SecurityRemoteManager(defaultRemoteManager), 1));
	}
	
}