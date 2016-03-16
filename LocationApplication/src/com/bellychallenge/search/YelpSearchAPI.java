package com.bellychallenge.search;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.util.Log;

import com.bellychallenge.constants.Constants;


/**
 * Class for fetching JSON data from Yelp Search API
 * 
 * @author Akshay
 *
 */
public class YelpSearchAPI {
	
	OAuthService authService;
	Token accessToken;
	
	/**
	 * 
	 * @param consumerKey
	 * @param consumerSecret
	 * @param token
	 * @param tokenSecret
	 */
	public YelpSearchAPI(String consumerKey,String consumerSecret,String token,String tokenSecret) {
		this.authService =
		        new ServiceBuilder().provider(OAuth.class).apiKey(consumerKey)
		            .apiSecret(consumerSecret).build();
		    this.accessToken = new Token(token, tokenSecret);
	}
	
	/**
	 * Method to generate an authentication request and query the yelp API
	 * @param latitude :Latitude
	 * @param longitude :Longitude
	 * @return :returns the JSON response
	 */
	public String fetchJSONData(String latitude,String longitude) {
		String ll=latitude+","+longitude;
	    OAuthRequest request = createOAuthRequest(Constants.SEARCH_PATH);
	    request.addQuerystringParameter("ll", ll);
	    request.addQuerystringParameter("radius_filter", String.valueOf(Constants.RADIUS_FILTER));
	    request.addQuerystringParameter("limit", String.valueOf(Constants.LIMIT));
	    request.addQuerystringParameter("sort",String.valueOf(Constants.SORT_OPTIONS));
	    return queryYelpAPI(request);
	  }

	/**
	 * Method to send response to YELP API and fetch JSOn data
	 * @param request :Authenticated request
	 * @return
	 */
	private String queryYelpAPI(OAuthRequest request) {
		Log.i("Complete URL: ",request.getCompleteUrl());
		this.authService.signRequest(this.accessToken, request);
	    Response response = request.send();
	    return response.getBody();
	}

	/**
	 * Method to create an Auth request with the given URL
	 * @param searchPath :path to search
	 * @return :Returns an authenticated request
	 */
	private OAuthRequest createOAuthRequest(String searchPath) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + Constants.API_HOST + searchPath);
	    return request;		
	}

	

}
