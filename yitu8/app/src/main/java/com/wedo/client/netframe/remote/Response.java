package com.wedo.client.netframe.remote;


import com.wedo.client.netframe.common.MessageCodes;

import java.util.Date;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:26:38
 */
public class Response {
	public Response() {
		super();
	}

	public Response(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	
	
	public Response(String body) {
		super();
		this.body = body;
	}



	private int code = com.wedo.client.netframe.common.MessageCodes.UNKNOW_ERROR;
	
	private String message;
	
	private Date queryTime;
	
	private String sessionId;
	
	private Object model;
	
	private  String body;
			
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getCode() {
		return code;
	}
	
	public boolean isSuccess() {
		return (code == MessageCodes.SUCCESS);
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", message=" + message + ", queryTime=" + queryTime + ", sessionId=" + sessionId + ", model=" + model + ", body=" + body + "]";
	}
	
	
	
}
