package com.bellychallenge.main;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bellychallenge.adapter.CustomListAdapter;
import com.bellychallenge.constants.Constants;
import com.bellychallenge.maps.CoordinateFetcher;
import com.bellychallenge.model.Business;
import com.bellychallenge.search.YelpSearchAPI;
import com.bellychallenge.util.CacheStorage;

/**
 * Home screen which displays the data for all businesses
 * 
 * @author Akshay
 *
 */
public class MainActivity extends Activity {
	protected static final String TAG = MainActivity.class.getSimpleName();
	/* ListView holding all the data */
	private ListView listview;
	private ProgressDialog progressDialog;
	private CustomListAdapter adapter;
	/* ArrayList holding the business Object for each business */
	public ArrayList<Business> businessInfoList = new ArrayList<Business>();
	JsonObjectRequest businessInfoReq;
	/* Object for YelpAPI */
	YelpSearchAPI searchAPI;
	private String responseJSON;
	protected CoordinateFetcher cooridinateFetcher;
	/* Flag to check the status of gps */
	private boolean gpsEnabledFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.main_container_listview);

		adapter = new CustomListAdapter(this, businessInfoList);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG, "Clicked item:" + position);
				Business business = businessInfoList.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable("businessObj", business);
				Intent intent = new Intent(view.getContext(),
						BusinessInformation.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		/* Instantiating coordinate fetcher object for fetching location */
		cooridinateFetcher = new CoordinateFetcher(MainActivity.this);
		if (cooridinateFetcher.getGPSStatus())
			gpsEnabledFlag = true;
		else
			gpsEnabledFlag = false;

		/* Fetching the current location of user */
		Location fetchedCurrentLocation = cooridinateFetcher
				.fetchCurrentLocation();

		/* Check for network connection */
		if (checkNetworkConnection() && gpsEnabledFlag) {
			/* Fetching current location of user device */
			// String currentLocation = cooridinateFetcher.getLocation();
			// String[] locationSplitter = currentLocation.split(",");
			// String currentLatitude = locationSplitter[0];
			// String currentLongitude = locationSplitter[1];
			double currentLatitude, currentLongitude;

			currentLatitude = fetchedCurrentLocation.getLatitude();
			currentLongitude = fetchedCurrentLocation.getLongitude();
			/* Executing through async task */
			asyncDataFetcher fetcher = new asyncDataFetcher(this,
					currentLatitude, currentLongitude);
			fetcher.execute();
		} else {
			/* Alert Dialog if no GPS or network is available */
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Network  Error/Gps Unvailable");
			builder.setMessage("No Network Or GPS Connection.")
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									displayFromCache();
								}

							});
			AlertDialog alert = builder.create();
			alert.show();

		}

	}

	/**
	 * Async Task for fetching JSON data from Yelp API, and displaying data in
	 * custom ListAdapter
	 * 
	 * @author Akshay
	 *
	 */
	private class asyncDataFetcher extends AsyncTask<String, Void, JSONArray> {

		private ProgressDialog progressDialog;
		private JSONParser parser;
		private double lLatitude = 0.0;
		private double lLongitude = 0.0;
		/* ArrayList holding cached buisness objects */
		private ArrayList<Business> cachedDataList;

		public asyncDataFetcher(MainActivity activity, double latitude,
				double longitude) {
			progressDialog = new ProgressDialog(activity);
			lLatitude = latitude;
			lLongitude = longitude;
			cachedDataList = new ArrayList<Business>();
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}

		@Override
		protected JSONArray doInBackground(String... params) {

			/* Quering YELP API with autheentication parameters */
			searchAPI = new YelpSearchAPI(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET, Constants.TOKEN,
					Constants.TOKEN_SECRET);

			/* Checking for default latitude and longitude */
			if (lLatitude == 0.0 && lLongitude == 0.0) {
				Toast.makeText(getApplicationContext(),
						"Location could not be fetched", Toast.LENGTH_LONG)
						.show();
				MainActivity.this.finish();
			}

			/* JSON response data from YELP API */
			responseJSON = searchAPI.fetchJSONData(String.valueOf(lLatitude),
					String.valueOf(lLongitude));
			Log.i(TAG, "JSON TO PARSE:" + responseJSON);

			/* JSON parser for parsing the JSON response */
			parser = new JSONParser();
			JSONObject response = null;

			try {
				response = (JSONObject) parser.parse(responseJSON);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			JSONArray jsonArray = (JSONArray) response
					.get(Constants.JSON_ARRAY_TAG);

			return jsonArray;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			progressDialog.dismiss();

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				Business business = new Business();

				String name = (String) jsonObj.get("name");
				if (name != null)
					business.setmName(jsonObj.get("name").toString());
				else
					business.setmName("(Name Unavailable)");

				String imageUrl = (String) jsonObj.get("image_url");
				if (imageUrl != null)
					business.setmbusinessLogoURL(jsonObj.get("image_url")
							.toString());
				else
					business.setmbusinessLogoURL("Not Available");

				String phone = (String) jsonObj.get("display_phone");
				if (phone != null)
					business.setmPhoneNumber(jsonObj.get("display_phone")
							.toString());
				else
					business.setmPhoneNumber("(Phone Unavailable)");

				if (!jsonObj.get("is_closed").equals("null")) {
					String status = jsonObj.get("is_closed").toString();

					String currentStatus = "";
					if (status.contains("true"))
						currentStatus = "Closed";
					else if (status.contains("false"))
						currentStatus = "Open";
					business.setmStatus(currentStatus);
				} else
					business.setmStatus("Unknown");

				if (!jsonObj.get("distance").equals("null"))
					business.setmDistance(jsonObj.get("distance").toString());
				else
					business.setmDistance("(Distance Unavailable)");

				String zipCode = (String) jsonObj.get("postal_code");
				if (zipCode != null)
					business.setmZipCode(Integer.parseInt(zipCode));
				else
					business.setmZipCode(0);

				JSONObject locationObj = (JSONObject) jsonObj.get("location");
				String state = (String) locationObj.get("state_code");
				if (state != null)
					business.setmState(state);
				else
					business.setmState("(State Unavailable)");

				String city = (String) locationObj.get("city");
				if (city != null)
					business.setmCity(city);
				else
					business.setmCity("(City Unavailable)");

				JSONArray displayAddressJsonArray = (JSONArray) locationObj
						.get("display_address");
				if (displayAddressJsonArray != null) {
					String addr = "";
					for (int k = 0; k < displayAddressJsonArray.size(); k++)
						addr = addr + displayAddressJsonArray.get(k).toString()
								+ ",";
					business.setmAddress(addr);
				} else
					business.setmAddress("(Address Unavailable)");

				JSONObject locationJsonObject = (JSONObject) locationObj
						.get("coordinate");

				if (locationJsonObject != null) {
					business.setmLatitude((Double) locationJsonObject
							.get("latitude"));
					business.setmLongitude((Double) locationJsonObject
							.get("longitude"));
				} else {
					business.setmLatitude(0.0);
					business.setmLongitude(0.0);
				}

				JSONArray catagoryJsonArray = (JSONArray) jsonObj
						.get("categories");
				ArrayList<JSONArray> catagories = new ArrayList<JSONArray>();

				for (int j = 0; j < catagoryJsonArray.size(); j++) {
					catagories.add((JSONArray) catagoryJsonArray.get(j));
				}
				if (catagories.size() != 0)
					business.setmCatagoriesList(catagories);

				/* Adding each business object in cache */
				cachedDataList.add(business);
				CacheStorage cache = new CacheStorage(getApplicationContext());

				/* Writing object to cache */
				cache.writeObject(Constants.CACHEKEY, cachedDataList);
				businessInfoList.add(business);
				adapter.notifyDataSetChanged();
			}

		}

	}

	/**
	 * Method to display the data from cache if network is not available
	 */

	protected void displayFromCache() {

		CacheStorage cache = new CacheStorage(getApplicationContext());
		@SuppressWarnings("unchecked")
		/* Fetching the list of cached objects */
		ArrayList<Business> fetchedCachedList = (ArrayList<Business>) cache
				.readObject(Constants.CACHEKEY);
		if (fetchedCachedList != null) {
			for (Business business : fetchedCachedList)
				businessInfoList.add(business);
			adapter.notifyDataSetChanged();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("No Cache");
			builder.setMessage("No Cached Data found")
					.setCancelable(true)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									moveTaskToBack(false);
									MainActivity.this.finish();
								}

							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	/**
	 * Method for checking if network connection is available
	 * 
	 * @return: returns a boolean value, if the network is connected or not
	 */
	private boolean checkNetworkConnection() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected())
			return true;
		else
			return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideDialog();
	}

	/**
	 * Hide the progress dialog box
	 */
	private void hideDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
