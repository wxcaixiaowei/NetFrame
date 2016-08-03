package com.wedo.client.netframe.remote.http;

import android.util.Log;

import com.wedo.client.netframe.common.MessageCodes;
import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;
import com.wedo.client.netframe.remote.parser.JsonResponseParser;
import com.wedo.client.netframe.remote.parser.ResponseParser;
import com.wedo.client.netframe.util.CollectionUtil;
import com.wedo.client.netframe.util.IoUtil;
import com.wedo.client.netframe.util.StringUtil;
import com.wedo.client.netframe.remote.http.HttpRemoteRequest.Method;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:27:58
 */
public class HttpRemoteManager extends RemoteManager {

	private static final String JSESSION_STR = "jsessionid=";

	private ResponseParser responseParser = new JsonResponseParser();

	private int timeout = HttpClientUtil.TIMEOUT;
	
	public Request createQueryRequest(String target) {
		HttpRemoteRequest ret = new HttpRemoteRequest(target,Method.GET);
		initBaseParameters(ret);
		return ret;
	}
	
	public Request createPostRequest(String target) {
		HttpRemoteRequest ret = new HttpRemoteRequest(target,Method.POST);
		initBaseParameters(ret);
		return ret;
	}
	
	private void initBaseParameters(Request request) {
//		MobileUseInfo mobileUseInfo = MobileUseInfo.getMobileUseInfo();
//		request.addParameter("cd_mac", mobileUseInfo.getMac());
//		request.addParameter("cd_mobile", mobileUseInfo.getMobile());
//		request.addParameter("cd_network", mobileUseInfo.getNetWork());
//		request.addParameter("cd_width", youthdayApplication.getScreenHelper().getDeviceRealWidth());
//		request.addParameter("cd_height", youthdayApplication.getScreenHelper().getDeviceRealHeigh());
//		request.addParameter("cd_client", youthdayApplication.getVersionName());
//		request.addParameter("cd_client_version", youthdayApplication.getVersionCode());
//		request.addParameter("cd_phone_type", 1 );
//		GeoPointExt pt = mobileUseInfo.getLocation();
//		request.addParameter("cd_latitude", pt.getLatitudeE6());
//		request.addParameter("cd_longitude", pt.getLongitudeE6());
	}
	
	@Override
	public Response execute(Request request) {
		if (!(request instanceof HttpRemoteRequest)) {
			throw new IllegalArgumentException("必须是HttpRequest才能执行！，参数是：" + request);
		}
		try {
			// 添加用户来源标记
			request.addParameter("userRefer", 1);
			 HttpRemoteRequest httpRequest = ( HttpRemoteRequest)request;
			HttpRequestBase httpRequestBase = asHttpRequest(httpRequest);
			String sessionId = request.getSessionId();
			if (!StringUtil.isEmpty(sessionId)) {
				httpRequestBase.addHeader("Cookie", JSESSION_STR + sessionId);
			}
			return handleHttpRequest(httpRequestBase);
		} catch (IOException e) {
			Log.e("HttpRemoteManager", "execute http", e);
			return new Response(MessageCodes.CONNECT_FAILED, "连接服务器失败");
		}
	}
	
	private HttpRequestBase asHttpRequest( HttpRemoteRequest request) throws IOException {
		 HttpRemoteRequest.Method method = request.getMethod();
		if (method ==  HttpRemoteRequest.Method.GET) {
			return   HttpClientUtil.createGetMethod(request.getTarget(), request.getParameters());
		}
		if (method ==  HttpRemoteRequest.Method.POST) {
			if (CollectionUtil.isEmpty(request.getBinaryItems())) {
				return   HttpClientUtil.createPostMethod(request.getTarget(), request.getParameters());
			}
			return   HttpClientUtil.createMultipartPostMethod(request.getTarget(), request.getParameters(), request.getBinaryItems(), request.getUploadFileCallback());
		}
		throw new IllegalArgumentException("未知Method:" + method);
	}
	
	private Response handleHttpRequest(HttpRequestBase httpRequestBase) throws IOException {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		
		byte[] content = null;
		String charset;
		HttpResponse httpResponse;
		Header cookie;
		try {
			httpResponse = httpClient.execute(httpRequestBase);
			cookie = httpResponse.getLastHeader("Set-Cookie");
//			Log.d("remote", "cookie: " + cookie);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				InputStream is = httpEntity.getContent();
				IoUtil.ioAndClose(is, bos);
				content = bos.toByteArray();
			}
			charset = EntityUtils.getContentCharSet(httpEntity);
		} finally {
			try {
				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
				// ignore it
			}
		}
			return responseParser.parse(content, charset, parseSessionId(cookie));
			
		
		
	}
	
	protected String parseSessionId(Header cookie) {
		if (cookie == null) {
			return null;
		}
		String cookieValue = cookie.getValue();
		List<String> parts = StringUtil.splitTrim(cookieValue, ";");
		for (String part : parts) {
			if (StringUtil.isEmpty(part)) {
				continue;
			}
			String lowerCase = part.toLowerCase();
			if (lowerCase.startsWith(JSESSION_STR)) {
				return part.substring(JSESSION_STR.length());
			}
		}
		return null;
	}

	public   ResponseParser getResponseParser() {
		return responseParser;
	}

	public void setResponseParser(  ResponseParser responseParser) {
		this.responseParser = responseParser;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
