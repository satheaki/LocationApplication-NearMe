package com.bellychallenge.maps;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Class for retrieving the current location coordinates of user
 * 
 * @author Akshay
 *
 */
public class CoordinateFetcher extends Service implements LocationListener {
	private final String TAG = CoordinateFetcher.class.getSimpleName();
	private final Activity mActivity;
	Location mLocation;
	private double mLatitude;
	private double mLongitude;
	private LocationManager mLocationManager;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;

	public CoordinateFetcher(Activity activity) {
		this.mActivity = activity;
		mLatitude = 0.0;
		mLongitude = 0.0;
		mLocationManager = (LocationManager) mActivity
				.getSystemService(LOCATION_SERVICE);
	}

	/**
	 * Method for fetching GPS status
	 * @return :returns boolean
	 */
	public boolean getGPSStatus() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	/**
	 * Method for returning location of user
	 * 
	 * @return: Returns Location object
	 */
	public Location fetchCurrentLocation() {
		try {

			/* Checking if network is available */
			isNetworkEnabled = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			/* Fetching network from network provider if network enabled */
			if (isNetworkEnabled) {
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000, 10, this);
				if (mLocationManager != null) {
					mLocation = mLocationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (mLocation != null) {
						mLatitude = mLocation.getLatitude();
						mLongitude = mLocation.getLongitude();
					}

				}
			}
			/* fetching location from GPS. */
			if (isGPSEnabled) {
				if (mLocation == null) {
					mLocationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 5000, 10, this);
					if (mLocationManager != null) {
						mLocation = mLocationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (mLocation != null) {
							mLatitude = mLocation.getLatitude();
							mLongitude = mLocation.getLongitude();
						}
					}
				}
			}


		} catch (Exception e) {
			Log.i(TAG, "Not possible to fetch Location");
			e.printStackTrace();
		}
		return mLocation;

	}

	/**
	 * Method to fetch latitude and longitude for a user
	 * 
	 * @return: latitude and longitude as a String
	 */
	public String getLocation() {
		Log.i(TAG, "Location from getLocation: " + mLatitude + "," + mLongitude);
		return mLatitude + "," + mLongitude;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "Location changed");
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}