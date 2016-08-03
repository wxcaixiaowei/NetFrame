package com.wedo.client.netframe.localcache;

import android.content.Context;

import java.io.UnsupportedEncodingException;

public class FileCache {

	private final static String UID_FILE_NAME = "u.txt";

	private final static String PASSPORT = "jiami.txt";
	
	private final static String USER_INFO = "user";

	private static  LocalCacheManager localCacheManager;

	public static void init(Context context) {
		String IMAGE_CACHE_DIR =   "netframe";
		localCacheManager = LocalCacheManagerFactory.createLocalCacheManager(context, IMAGE_CACHE_DIR);
	}

	public static void putUser(String uid) {
		assertInit();
		localCacheManager.putData(UID_FILE_NAME, uid.getBytes());
	}

	public static void putPassport(String passport) {
		assertInit();
		localCacheManager.putData(PASSPORT, passport.getBytes());
	}

	public static String getUser() {
		byte[] data = localCacheManager.getData(UID_FILE_NAME);
		if (data != null) {
			try {
				return new String(data, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				return data.toString();
			}
		}
		return null;
	}

	public static String getPassport() {
		byte[] data = localCacheManager.getData(PASSPORT);
		if (data != null) {
			try {
				return new String(data, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				return data.toString();
			}
		}
		return null;
	}

	//可以在此本地化数据存储 也可以使用sharePreferen

/*	public static void addUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}

		ObjectOutputStream oos = null;
		try {
			oos = localCacheManager.getOutputStream(USER_INFO);
			oos.writeUTF(userInfo.getName());
			oos.writeUTF(userInfo.getPhone());
			oos.writeUTF(userInfo.getAddress());
			oos.flush();
		} catch (Exception e) {
		} finally {
			IoUtil.closeQuietly(oos);
		}
	}*/
	
/*	public static UserInfo getUserInfo() {
		ObjectInputStream ois = null;
		try {
			ois = localCacheManager.getInputStream(USER_INFO);
			String name = (String) ois.readUTF();
			String phone = (String) ois.readUTF();
			String address = (String) ois.readUTF();
			UserInfo userInfo = new UserInfo();
			userInfo.setName(name);
			userInfo.setPhone(phone);
			userInfo.setAddress(address);
			return userInfo;
		} catch (Exception e) {
		} finally {
			IoUtil.closeQuietly(ois);
		}
		return null;
		
	}*/

	private static void assertInit() {
		if (localCacheManager == null) {
			throw new RuntimeException("You must call ImageCache.init first!");
		}
	}

}
