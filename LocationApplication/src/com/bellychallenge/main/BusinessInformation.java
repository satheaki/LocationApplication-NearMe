package com.bellychallenge.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bellychallenge.application.Controller;
import com.bellychallenge.maps.GoogleMapsActivity;
import com.bellychallenge.model.Business;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Class for Storing and displaying the data for each particular business
 * 
 * @author Akshay
 *
 */
public class BusinessInformation extends FragmentActivity implements
		OnMapReadyCallback {
	protected static final String TAG = BusinessInformation.class
			.getSimpleName();
	private TextView businessName;
	private TextView cityState;
	private TextView addressZipCode;
	private TextView phoneNumber;
	private TextView latitudeLongitude;
	private ImageLoader imageLoader;
	private ImageView mLocation;
	private MapFragment mMapFragment;
	String lat = "", longi = "";
	String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_item_info);
		businessName = (TextView) findViewById(R.id.business_name_id);
		cityState = (TextView) findViewById(R.id.business_state_city);
		addressZipCode = (TextView) findViewById(R.id.business_address);
		phoneNumber = (TextView) findViewById(R.id.business_phonenumber);
		latitudeLongitude = (TextView) findViewById(R.id.business_lat_longi);
		imageLoader = Controller.getInstance().getImageLoader();
		NetworkImageView businessLogo = (NetworkImageView) findViewById(R.id.business_logo);
		mLocation = (ImageView) findViewById(R.id.image_location_marker);

		/* Fetching data passed through intent */
		Business business = (Business) getIntent().getExtras().getSerializable(
				"businessObj");

		lat = "" + business.getmLatitude();
		longi = "" + business.getmLongitude();

		/* Initializing a mapView Fragment */
		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(
				R.id.map_fragment);
		mMapFragment.getMapAsync(this);

		name = business.getmName();
		businessName.setText(name);
		String city = business.getmCity();
		String state = business.getmState();
		cityState.setText(city + ", " + state);
		String address = business.getmAddress();
		addressZipCode.setText(address);
		phoneNumber.setText(business.getmPhoneNumber());

		latitudeLongitude.setText("View on Map");
		businessLogo.setImageUrl(business.getmbusinessLogoURL(), imageLoader);

		/* Onclicklistner for marker button */
		mLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BusinessInformation.this,
						GoogleMapsActivity.class);
				intent.putExtra("latitude", lat);
				intent.putExtra("longitude", longi);
				intent.putExtra("businessname", name);
				startActivity(intent);

			}
		});

	}

	@Override
	public void onMapReady(GoogleMap map) {
		double lLati = Double.parseDouble(lat);
		double lLongi = Double.parseDouble(longi);
		LatLng location = new LatLng(lLati, lLongi);
		Log.i(TAG, "latitude: " + lat + " longitude: " + longi);
		Toast.makeText(getApplicationContext(), "Name:" + name,
				Toast.LENGTH_LONG).show();

		map.addMarker(new MarkerOptions().position(new LatLng(lLati, lLongi))
				.title(name));
		map.moveCamera(CameraUpdateFactory.newLatLng(location));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 200, null);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setRotateGesturesEnabled(true);

	}
}
