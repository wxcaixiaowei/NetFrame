package com.wedo.client.netframe.activities.common;


import com.wedo.client.netframe.remote.Response;

public interface ThreadListener {
	
	void onPostExecute(Response response);
}