package com.bellychallenge.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

/**
 * Container class for storing and retrieving all model data values
 * @author Akshay
 *
 */
public class Business implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String mName, mLogoURL, mAddress, mCity, mState, mPhoneNumber,mDistance,mCatagory,mStatus;
	private double mLatitude, mLongitude;
	private int mbusinessId, mZipCode;
	private List<JSONArray> mCatagoriesList=new ArrayList<JSONArray>();

	public Business(){
	}
	/*Constructor*/
	public Business(String name, String address, String city, String state,
			String imageURL, double latitude, double longitude, int id,
			int zipcode,String distance,String catagory,String status,ArrayList<JSONArray>catagories) {
		this.mName = name;
		this.mAddress = address;
		this.mCity = city;
		this.mState = state;
		this.mLogoURL = imageURL;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mbusinessId = id;
		this.mZipCode = zipcode;
		this.mDistance=distance;
		this.mCatagory=catagory;
		this.mStatus=status;
		this.mCatagoriesList=catagories;
				
	}

	/**
	 * 
	 * @return
	 */
	public List<JSONArray> getmCatagoriesList() {
		return mCatagoriesList;
	}
	/**
	 * 
	 * @param catagories
	 */
	public void setmCatagoriesList(ArrayList<JSONArray> catagories) {
		this.mCatagoriesList = catagories;
	}
	/**
	 * Getter for name of business
	 * @return: returns business name
	 */
	public String getmName() {
		return mName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getmLogoURL() {
		return mLogoURL;
	}
	/**
	 * 
	 * @param mLogoURL
	 */
	public void setmLogoURL(String mLogoURL) {
		this.mLogoURL = mLogoURL;
	}
	/**
	 * 
	 * @return
	 */
	public String getmDistance() {
		return mDistance;
	}
	/**
	 * 
	 * @param mDistance
	 */
	public void setmDistance(String mDistance) {
		this.mDistance = mDistance;
	}
	/**
	 * 
	 * @return
	 */
	public String getmCatagory() {
		return mCatagory;
	}
	/**
	 * 
	 * @param mCatagory
	 */
	public void setmCatagory(String mCatagory) {
		this.mCatagory = mCatagory;
	}
	/**
	 * 
	 * @return
	 */
	public String getmStatus() {
		return mStatus;
	}
	/**
	 * 
	 * @param mStatus
	 */
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	/**
	 * Setter for business name
	 * @param mName: business name
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * Getter for businessLogo
	 * @return: return Image URL
	 */
	public String getmbusinessLogoURL() {
		return mLogoURL;
	}

	/**
	 * Setter for businessLogo
	 * @param mbusinessLogoURL:business Image URL
	 */
	public void setmbusinessLogoURL(String mbusinessLogoURL) {
		this.mLogoURL = mbusinessLogoURL;
	}

	/**
	 * Getter for address
	 * @return:Returns business address
	 */
	public String getmAddress() {
		return mAddress;
	}

	/**
	 * Setter for business address
	 * @param mAddress:business address
	 */
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	/**
	 * Getter for city
	 * @return: Returns business city
	 */
	public String getmCity() {
		return mCity;
	}

	/**
	 * Setter for city
	 * @param mCity:city of business
	 */
	public void setmCity(String mCity) {
		this.mCity = mCity;
	}

	/**
	 * Getter of State
	 * @return :returns the state
	 */
	public String getmState() {
		return mState;
	}

	/**
	 * Setter for state
	 * @param mState:business state
	 */
	public void setmState(String mState) {
		this.mState = mState;
	}

	/**
	 * Getter for phone number
	 * @return:Returns phonenumber
	 */
	public String getmPhoneNumber() {
		return mPhoneNumber;
	}

	/**
	 * Setter for Phone Number
	 * @param mPhoneNumber :Phonenumber of business
	 */
	public void setmPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}

	/**
	 * getter for latitude
	 * @return :Returns latitude 
	 */
	public double getmLatitude() {
		return mLatitude;
	}

	/**
	 * Setter for latitude
	 * @param mLatitude: latitude of business location
	 */
	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	/**
	 * Getter of Longitude
	 * @return :Returns the longitude
	 */
	public double getmLongitude() {
		return mLongitude;
	}

	/**
	 * Setter of longitude
	 * @param mLongitude :Longitude of business
	 */
	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	/**
	 * Getter of business ID
	 * @return :returns business ID
	 */
	public int getmbusinessId() {
		return mbusinessId;
	}

	/**
	 * Setter for business ID
	 * @param mbusinessId :ID of business
	 */
	public void setmbusinessId(int mbusinessId) {
		this.mbusinessId = mbusinessId;
	}

	/**
	 * Getter of ZipCode
	 * @return :returns zipcode for business
	 */
	public int getmZipCode() {
		return mZipCode;
	}

	/**
	 * Setter for ZipCode
	 * @param mZipCode :Zipcode of business
	 */
	public void setmZipCode(int mZipCode) {
		this.mZipCode = mZipCode;
	}

}
