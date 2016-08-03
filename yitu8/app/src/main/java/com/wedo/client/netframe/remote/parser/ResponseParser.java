package com.wedo.client.netframe.remote.parser;

import com.wedo.client.netframe.remote.Response;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:30:02
 */
public interface ResponseParser {

	Response parse(byte[] content, String charset, String sessionId);
	
}
