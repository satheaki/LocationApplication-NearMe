package com.bellychallenge.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

/**
 * Class for implementing a cache storage as an internal storage
 * 
 * @author Akshay
 *
 */
public class CacheStorage {
	FileOutputStream fOutStream;
	Context mContext;

	public CacheStorage(Context context) {
		mContext = context;
	}

	/**
	 * Method for writing object to internal memory
	 * 
	 * @param context
	 *            :Application Context
	 * @param KEY
	 *            :Unique identifier for writing and reading object to file
	 * @param object
	 *            :Object to be written to file
	 */
	public void writeObject(String KEY, Object object) {
		File file;
		ObjectOutputStream objectOutStream;

		try {
			if (!mContext.getCacheDir().exists())
				mContext.getCacheDir().mkdirs();
			file = new File(mContext.getCacheDir(), KEY);
			fOutStream = new FileOutputStream(file);
			objectOutStream = new ObjectOutputStream(fOutStream);
			objectOutStream.writeObject(object);
			objectOutStream.close();
			fOutStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for reading an object from internal storage.
	 * 
	 * @param context
	 *            :Application context
	 * @param KEY
	 *            :Unique identifier for writing and reading object to file
	 * @return : Returns the cached object
	 */
	public Object readObject(String KEY) {
		File file = null;
		Object obj = null;
		FileInputStream fInputStream;
		try {
			file = new File(mContext.getCacheDir().getAbsolutePath(), KEY);
			fInputStream = new FileInputStream(file);
			ObjectInputStream objectInStream = new ObjectInputStream(
					fInputStream);
			obj = objectInStream.readObject();
			fInputStream.close();
			objectInStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;

	}

}
