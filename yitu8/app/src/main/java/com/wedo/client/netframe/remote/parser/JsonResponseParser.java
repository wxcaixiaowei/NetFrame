package com.wedo.client.netframe.remote.parser;

import android.util.Log;

import com.wedo.client.netframe.common.MessageCodes;
import com.wedo.client.netframe.remote.Response;
import com.wedo.client.netframe.remote.http.HttpClientUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author wxc
 * 这个类的作用是由服务端统一了返回结果的格式，所以统一做一个处理之后再作解析读取
 * @date 2013-5-29 下午11:30:26
 */
public class JsonResponseParser implements ResponseParser {

	private static final String CODE = "code";
//	
	private static final String MESSAGE = "msg";
//	
//	private static final String QUERY_TIME = "queryTime";
	
	@Override
	public Response parse(byte[] content, String charset, String sessionId) {
		try {
			String contentString = null;
			try {
				if (charset == null) {
					charset = HttpClientUtil.DEFAULT_CHARSET;
				}
				contentString = new String(content, charset);
			} catch (UnsupportedEncodingException e) {
				try {
					contentString = new String(content, HttpClientUtil.DEFAULT_CHARSET);
				} catch (UnsupportedEncodingException e1) {
					throw new RuntimeException(e1);
				}
			}
			
//			if(contentString != null) {
//				contentString = contentString.trim();
//			}
//			Log.d("json parse", contentString);
//			Log.d("json parse", "sessionId:" + sessionId);
			JSONObject jsonObject = new JSONObject(contentString);
			int code =0;
			if(jsonObject.has(CODE)){
				code = jsonObject.getInt(CODE);
			}else {
				Response response = new Response(contentString);
				return response;
				}
			String message = null;
			if (jsonObject.has(MESSAGE)) {
				message = jsonObject.getString(MESSAGE);
			}
			Response response = new Response(code, message);
//			if (jsonObject.has(QUERY_TIME)) {
//				String queryTimeStr = jsonObject.getString(QUERY_TIME);
//				Date queryTime = DateUtil.parse(queryTimeStr);
//				response.setQueryTime(queryTime);
//			}
//			response.setSessionId(sessionId);
			response.setModel(jsonObject);
//			Log.e("LOG_TAG",response.toString());

			return response;
		} catch (Exception e) {
			Log.e("json parse", e.getMessage(), e);
			return new Response(MessageCodes.JSON_PARSE_FAILED, e.getMessage());
		}
		
	}

}

