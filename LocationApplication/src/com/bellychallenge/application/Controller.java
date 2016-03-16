package com.bellychallenge.application;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bellychallenge.util.LRUCache;

/**
 * A Controller class which manages the entire application data. It is
 * implemented as a singleton class having only one instance of an object
 * 
 * @author Akshay
 *
 */
public class Controller extends Application {

	private static final String TAG = Controller.class.getSimpleName();
	/* Singleton instance of a class */
	private static Controller mControllerInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mControllerInstance = this;
	}

	/**
	 * This method return a single instance of the class
	 * 
	 * @return: returns a object of the Controller class
	 */
	public static synchronized Controller getInstance() {

		return mControllerInstance;
	}

	/**
	 * This method sets up a new RequestQueue if not already
	 * established,otherwise returns a already established queue
	 * 
	 * @return: Returns a request queue
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null)
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		return mRequestQueue;

	}

	/**
	 * This method sets up a ImageLoader which is a helper class that handles
	 * loading and caching images from remote URLs
	 * 
	 * @return: Returns the object of imageloader class
	 */
	public ImageLoader getImageLoader() {
		getRequestQueue();
		ImageLoader.ImageCache imgCache = new LRUCache();
		if (mImageLoader == null)
			mImageLoader = new ImageLoader(this.mRequestQueue, imgCache);
		return mImageLoader;

	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		/* set the default tag if tag is empty */
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * Cancels all the pending requests, if the request queue is not null.
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}
