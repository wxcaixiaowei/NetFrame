package com.wedo.client.netframe.localcache;

import android.content.Context;
import android.util.Log;

import com.wedo.client.netframe.util.IoUtil;
import com.wedo.client.netframe.util.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/**
 * @author wxc
 *
 * @date 2013-5-29 下午11:48:46
 */
public class LocalCacheManagerFs implements LocalCacheManager {

	private Context context;
	
	private String dirName;
	
	public LocalCacheManagerFs(Context context, String dirName) {
		super();
		this.context = context;
		this.dirName = dirName;
	}

	@Override
	public void clearLocalCache() {
		File f = context.getDir(dirName, 0);
		IoUtil.delete(f);
	}

	@Override
	public void delete(String key) {
		context.deleteFile(makeFileName(key));
	}

	@Override
	public byte[] getData(String key) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		String filename = makeFileName(key);
		File file = context.getFileStreamPath(filename);
		if (file == null) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		InputStream is = null;
		try {
			is = context.openFileInput(filename);
			return IoUtil.readAll(is);
		} catch(FileNotFoundException e) {
			Log.i("local cache", "file not exist:" + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("local cache", e.getMessage(), e);
			return null;
		} finally {
			IoUtil.closeQuietly(is);
		}
	}

	@Override
	public void putData(String key, byte[] data) {
		if (StringUtil.isEmpty(key) || data == null) {
			return;
		}
		OutputStream os = null;
		try {
			os = context.openFileOutput(makeFileName(key), Context.MODE_PRIVATE);
			os.write(data);
		} catch (IOException e) {
			Log.e("local cache", e.getMessage(), e);
		} finally {
			IoUtil.closeQuietly(os);
		}
	}
	
	@Override
	public File getTargetFile(String name) {
		return context.getFileStreamPath(name);
	}

	
	@Override
	public ObjectInputStream getInputStream(String key) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		String filename = makeFileName(key);
		File file = context.getFileStreamPath(filename);
		if (file == null) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		try {
			return new ObjectInputStream(context.openFileInput(filename));
		} catch (FileNotFoundException e) {
			Log.i("local cache", "file not exist:" + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("local cache", e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public ObjectOutputStream getOutputStream(String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		try {
			return new ObjectOutputStream(context.openFileOutput(makeFileName(name), Context.MODE_PRIVATE));
		} catch (IOException e) {
			Log.e("local cache", e.getMessage(), e);
		}
		return null;
	}
	
	protected String makeFileName(String key) {
		return dirName + "__" + key;
	}

}

