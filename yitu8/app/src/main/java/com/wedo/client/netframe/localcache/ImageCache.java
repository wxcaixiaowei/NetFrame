package com.wedo.client.netframe.localcache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wedo.client.netframe.util.cache.LRUCache;

import java.io.File;



/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:45:53
 */
public class ImageCache {

	private static  LocalCacheManager localCacheManager;

	private static LRUCache<String, Bitmap> memCache = new LRUCache<String, Bitmap>();

	public static void init(Context context) {
		String IMAGE_CACHE_DIR =   "netframe";
		localCacheManager =  LocalCacheManagerFactory.createLocalCacheManager(context, IMAGE_CACHE_DIR);
	}

	public static boolean fileExist(String name, int size) {
		String makeKeyName = makeKeyName(name, size);
		File file = localCacheManager.getTargetFile(makeKeyName);
		if (file != null && file.exists()) {
			return true;
		}
		return false;
	}
	
	public static Bitmap getBitmap(String name) {
		String makeKeyName = makeKeyName(name);
		Bitmap ret = memCache.get(makeKeyName);
		if (ret != null) {
			return ret;
		}
		byte[] data = getBitmapData(makeKeyName);
		if (data == null) {
			ret = null;
		} else {
			ret = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		memCache.put(makeKeyName, ret);
		return ret;
	}
	
	public static String makeKeyName(String name) {
		return name;
	}

	public static Bitmap getBitmap(String name, int size) {
		String makeKeyName = makeKeyName(name, size);
		Bitmap ret = memCache.get(makeKeyName);
		if (ret != null) {
			return ret;
		}
		byte[] data = getBitmapData(makeKeyName);
		if (data == null) {
			ret = null;
		} else {
			ret = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		memCache.put(makeKeyName, ret);
		return ret;
	}

	public static String makeKeyName(String name, int size) {
		return name + "_" + size + "x" + size + ".jpg";
	}
	
	public static String makeKeySuffix(int size) {
		return  "_" + size + "x" + size + ".jpg";
	}

	public static byte[] getBitmapData(String name) {
		assertInit();
		byte[] ret = localCacheManager.getData(name);
		return ret;
	}

	public static void setBitmapData(String name, byte[] data) {
		assertInit();
		localCacheManager.putData(name, data);
	}

	private static void assertInit() {
		if (localCacheManager == null) {
			throw new RuntimeException("You must call ImageCache.init first!");
		}
	}

	public static  LocalCacheManager getLocalCacheManager() {
		return localCacheManager;
	}

}
