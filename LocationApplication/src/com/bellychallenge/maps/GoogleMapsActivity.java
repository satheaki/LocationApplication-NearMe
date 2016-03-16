package com.bellychallenge.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bellychallenge.main.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Class for launching Google Maps and displaying location maps
 * 
 * @author Akshay
 *
 */
public class GoogleMapsActivity extends FragmentActivity implements
		OnMapReadyCallback {
	protected static final String TAG = GoogleMapsActivity.class
			.getSimpleName();
	private String latitude = "";
	private String longitude = "";
	private String businessName = "";
	
	/*Map fragment for displaying map in a view*/
	private MapFragment mMapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps);

		Intent intent = getIntent();
		latitude = intent.getStringExtra("latitude");
		longitude = intent.getStringExtra("longitude");
		businessName = intent.getStringExtra("businessname");

		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(
				R.id.map_fragment);
		mMapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap map) {
		double lat = Double.parseDouble(latitude);
		double longi = Double.parseDouble(longitude);
		LatLng location = new LatLng(lat, longi);
		Log.i(TAG, "latitude: " + lat + " longitude: " + longi);
		Toast.makeText(getApplicationContext(), "Name:" + businessName,
				Toast.LENGTH_LONG).show();

		/*Adding a marker on the map*/
		map.addMarker(new MarkerOptions().position(new LatLng(lat, longi))
				.title(businessName));
		
		/*Focusing the view on the specified location*/
		map.moveCamera(CameraUpdateFactory.newLatLng(location));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 200, null);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setRotateGesturesEnabled(true);
	}

}