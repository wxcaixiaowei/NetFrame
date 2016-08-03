package com.wedo.client.netframe.localcache;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:48:13
 */
public interface LocalCacheManager {
	
	byte[] getData(String key);

	void putData(String key, byte[] data);

	void delete(String key);

	File getTargetFile(String name);

	void clearLocalCache();
	
	ObjectInputStream getInputStream(String name);
	
	ObjectOutputStream getOutputStream(String name);

}
