package com.bellychallenge.util;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Class Implementing a default LRUCache in volley which implements a ImageCache
 * @author Akshay
 *
 */
public class LRUCache extends LruCache<String, Bitmap> implements ImageCache {

	/*Calculate default size of cache*/
	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		return cacheSize;
	}

	public LRUCache(){
		this(getDefaultLruCacheSize());
	}
	public LRUCache(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    protected int sizeOf(String key, Bitmap value) {
	        return value.getRowBytes() * value.getHeight() / 1024;
	    }

	@Override
	public Bitmap getBitmap(String logoURL) {
		return get(logoURL);
	}

	@Override
	public void putBitmap(String logoURL, Bitmap bitmap) {
		put(logoURL, bitmap);
	}

}
