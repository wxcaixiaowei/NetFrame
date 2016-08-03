package com.wedo.client.netframe.remote.http;

import com.wedo.client.netframe.common.ProgressCallback;
import com.wedo.client.netframe.common.ProgressCallbackHelper;
import com.wedo.client.netframe.common.ProgressSupportByteArrayBody;
import com.wedo.client.netframe.remote.BinaryItem;
import com.wedo.client.netframe.remote.Parameter;
import com.wedo.client.netframe.util.CollectionUtil;
import com.wedo.client.netframe.util.IoUtil;
import com.wedo.client.netframe.util.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:28:44
 */
public class HttpClientUtil {
public static final int TIMEOUT = 10000;
	
	public static final String DEFAULT_CHARSET = "utf-8";

	private static final String TIMEOUT_PARAM = "http.socket.timeout";
	
	private static final String DEFAULT_MIME_TYPE = "application/zip";
	
	public static HttpPost createMultipartPostMethod(String url,List<Parameter> parameters, List<BinaryItem> binaryItems, ProgressCallback progressCallback) {
		HttpPost httpPostRequest = new HttpPost(url);
		httpPostRequest.getParams().setParameter(TIMEOUT_PARAM, TIMEOUT);
		MultipartEntity multipartEntity = new MultipartEntity();
		try {
			for (NameValuePair nameValuePair : toHttpClientParameters(parameters)) {
				multipartEntity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue(), Charset.forName(DEFAULT_CHARSET)));
			}
		} catch (IllegalCharsetNameException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedCharsetException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		int maxLength = 0;
		ProgressCallbackHelper progressCallbackHelper = new ProgressCallbackHelper(binaryItems.size(), progressCallback);
		for (BinaryItem binaryItem : binaryItems) {
			ProgressSupportByteArrayBody progressSupportByteArrayBody = new ProgressSupportByteArrayBody(binaryItem.data, DEFAULT_MIME_TYPE, binaryItem.name);
			progressSupportByteArrayBody.setProgressCallback(progressCallback);
			progressSupportByteArrayBody.setProgressCallbackHelper(progressCallbackHelper);
			maxLength += progressSupportByteArrayBody.getMaxLength();
			multipartEntity.addPart(binaryItem.name, progressSupportByteArrayBody);
		}
		if(progressCallback != null) {
			progressCallback.onSetMaxSize(maxLength);
		}
		httpPostRequest.setEntity(multipartEntity);
		
		return httpPostRequest;
	}
	
	public static HttpPost createPostMethod(String url, List<Parameter> parameters) {
		HttpPost httpPostRequest = new HttpPost(url);
		httpPostRequest.getParams().setParameter(TIMEOUT_PARAM, TIMEOUT);
		if (!CollectionUtil.isEmpty(parameters)) {
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(toHttpClientParameters(parameters), DEFAULT_CHARSET);
				httpPostRequest.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		LogUtil.logTagE(httpPostRequest.getURI().toString());
		return httpPostRequest;
	}
	
	public static HttpGet createGetMethod(String url, List<Parameter> parameters) throws IOException {
		
		URI uri = createURI(url, toHttpClientParameters(parameters));
		HttpGet httpRequest = new HttpGet(uri);
		//httpRequest.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
		httpRequest.getParams().setParameter(TIMEOUT_PARAM, TIMEOUT);
		LogUtil.logTagE(httpRequest.getURI().toString());
		return httpRequest;
	}
	
	public static List<NameValuePair> toHttpClientParameters(List<Parameter> parameters) {
		List<NameValuePair> ret = CollectionUtil.newArrayList();
		if (CollectionUtil.isEmpty(parameters)) {
			return ret;
		}
		for (Parameter parameter : parameters) {
			ret.add(new BasicNameValuePair(parameter.name, parameter.value));
		}
		return ret;
	}
	
	public static URI createURI(String url, List<NameValuePair> parameters) {
		try {
			URL urlObj = new URL(url);
			String queryString = URLEncodedUtils.format(parameters, DEFAULT_CHARSET);
			return URIUtils.createURI(urlObj.getProtocol(), urlObj.getHost(), urlObj.getPort(), urlObj.getPath(), queryString, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] getHttpData(HttpRequestBase httpRequestBase) throws IOException {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		
		byte[] content = null;
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpRequestBase);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				InputStream is = httpEntity.getContent();
				IoUtil.ioAndClose(is, bos);
				content = bos.toByteArray();
			}
		} finally {
			try {
				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
				// ignore it
			}
		}
		return content;
	}
	
}
