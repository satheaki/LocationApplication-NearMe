package com.bellychallenge.adapter;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bellychallenge.application.Controller;
import com.bellychallenge.constants.Constants;
import com.bellychallenge.main.R;
import com.bellychallenge.model.Business;

/**
 * A CustomList Adapter class used for inflating different views in a single row
 * of a list view.
 * 
 * @author Akshay
 *
 */
@SuppressLint("InflateParams")
public class CustomListAdapter extends BaseAdapter {
	private final String TAG = CustomListAdapter.class.getSimpleName();
	private LayoutInflater mInflater;
	private Activity mActivityInstance;
	private ArrayList<Business> mBusinessList;

	ImageLoader mImageLoader;

	/* Constructor */
	public CustomListAdapter(Activity activity,
			ArrayList<Business> businessInfoList) {
		this.mActivityInstance = activity;
		this.mBusinessList = businessInfoList;
	}

	@Override
	public int getCount() {
		return mBusinessList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBusinessList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * getView method which displays the data at a specified view in the data
	 * set
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mInflater == null)
			mInflater = (LayoutInflater) mActivityInstance
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = mInflater.inflate(R.layout.custom_list_view, null);

		if (mImageLoader == null)
			mImageLoader = Controller.getInstance().getImageLoader();

		NetworkImageView businessLogoThumbnail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView businessName = (TextView) convertView
				.findViewById(R.id.business_name);
		TextView businessStatus = (TextView) convertView
				.findViewById(R.id.business_status);
		TextView businessdistance = (TextView) convertView
				.findViewById(R.id.distance);
		TextView businessCatagory = (TextView) convertView
				.findViewById(R.id.business_type);
		ImageView arrowImageIcon = (ImageView) convertView
				.findViewById(R.id.arrow_img);

		/* Retrieve business data for each row */
		Business business = mBusinessList.get(position);
		if (!business.getmbusinessLogoURL().contains("Not Available"))
			businessLogoThumbnail.setImageUrl(business.getmbusinessLogoURL(),
					mImageLoader);
		else
			businessLogoThumbnail.setBackgroundColor(Color.BLACK);

		businessName.setText(business.getmName());
		String status = business.getmStatus();
		if (status.contains("Open")) {
			businessStatus.setTextColor(Color.GREEN);
			businessStatus.setText(business.getmStatus());
		} else if (status.contains("Closed")) {
			businessStatus.setTextColor(Color.RED);
			businessStatus.setText(business.getmStatus());
		}
		double dist = Double.parseDouble(business.getmDistance());
		double distInMiles = dist * Constants.METERSTOMILES;
		businessdistance.setText(String.format("%.2f", distInMiles)
				+ " miles away");
		List<JSONArray> allCatagoryList = business.getmCatagoriesList();
		JSONArray singleCatagory = allCatagoryList.get(0);
		businessCatagory.setText(singleCatagory.get(0).toString());
		arrowImageIcon.setImageResource(R.drawable.arrow);

		return convertView;

	}

}
