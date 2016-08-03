package com.wedo.client.netframe.remote;

import com.wedo.client.netframe.common.ProgressCallback;
import com.wedo.client.netframe.util.CollectionUtil;

import java.util.List;



/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:20:47
 */
public class Request implements Cloneable {
	
	private String target;
	
	private String sessionId;
	
	private ProgressCallback uploadFileCallback;
	
	public Request(String target) {
		super();
		this.target = target;
	}

	private List<Parameter> parameters = CollectionUtil.newArrayList();
	
	private List<BinaryItem> binaryItems = CollectionUtil.newArrayList();

	@Override
	public String toString() {
		return "Request [parameters=" + parameters + ", target=" + target + "]";
	}

	public Request clone() {
		try {
			Request ret = (Request)super.clone();
			ret.parameters = CollectionUtil.newArrayList(parameters);
			ret.binaryItems = CollectionUtil.newArrayList(binaryItems);
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Request() {
		super();
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public Request addParameter(String name, Object value) {
		parameters.add(new Parameter(name, (value == null ? "" : String.valueOf(value))));
		return this;
	}
	
	public List<BinaryItem> getBinaryItems() {
		return binaryItems;
	}

	public Request addBinaryItem(String name, byte[] data) {
		binaryItems.add(new BinaryItem(name, data));
		return this;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ProgressCallback getUploadFileCallback() {
		return uploadFileCallback;
	}

	public void setUploadFileCallback(ProgressCallback uploadFileCallback) {
		this.uploadFileCallback = uploadFileCallback;
	}
}

