package com.wedo.client.netframe.remote.proxy;

import com.wedo.client.netframe.remote.BinaryItem;
import com.wedo.client.netframe.remote.Parameter;
import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;
import com.wedo.client.netframe.util.CollectionUtil;
import com.wedo.client.netframe.util.MD5;
import com.wedo.client.netframe.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * @author wxc
 *http://192.168.199.127:9090/weixin/ajax/login.html
 * @date 2013-5-29 下午11:36:31
 */
public class SecurityRemoteManager extends RemoteManager {

	private RemoteManager remoteManager;
	
	public SecurityRemoteManager(RemoteManager remoteManager) {
		super();
		this.remoteManager = remoteManager;
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

		return remoteManager.execute(request);
	}

	
	/**
	 * 签名方法，把所有参数名转成小写(参数有helloWorld的转成hello_world)，然后按字母排序，name和value(url encoding utf-8)用=链接，每个parameter用&连接，如果是二进制字段（比如文件上传）先把内容md5（小写）后当做值处理
	 * 最后把拼接的结果再最后面加上signKey后md5
	 */
	private String genSign(Request request, String signKey) {
		List<Parameter> parameters = request.getParameters();
		Map<String, String> sortedParameters = CollectionUtil.newTreeMap();
		for (Parameter parameter : parameters) {
			if (parameter == null) {
				continue;
			}
			if (StringUtil.isEmpty(parameter.name)) {
				continue;
			}
			try {
				sortedParameters.put(StringUtil.camelToUnderLineString(parameter.name).toLowerCase(), URLEncoder.encode(parameter.value, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// ignore
			}
		}
		List<BinaryItem> binaryItems = request.getBinaryItems();
		for (BinaryItem binaryItem : binaryItems) {
			if (binaryItem == null) {
				continue;
			}
			if (StringUtil.isEmpty(binaryItem.name)) {
				continue;
			}
			String dataMD5 = MD5.getMD5(binaryItem.data);
			sortedParameters.put(StringUtil.camelToUnderLineString(binaryItem.name).toLowerCase(), dataMD5.toLowerCase());
		}
		String orgString = CollectionUtil.join(sortedParameters, "=", "&");
		String orgStringKey = orgString + signKey;
	//	Log.d("sign", "orgString：" + orgString);
		return MD5.getMD5(orgStringKey.getBytes()).toLowerCase();
	}
	
	
}

