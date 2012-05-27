package com.sandstonelabs.mimi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiRestaurantSearch {

	public static final String BASE_URL = "http://www2.viamichelin.co.uk";
	public static final String SEARCH_URL = BASE_URL + "/vmw2/maf/dyn/controller/jseMultiPoiPaginationFinder";
	public static final String SINGLE_POI_URL = BASE_URL + "/vmw2/maf/dyn/controller/jseSinglePoiFinder";

	public List<String> searchRestaurants(float latitude, float longitude, int page) throws IOException {

		URL searchURL = new URL(SEARCH_URL);
		HttpURLConnection connection = (HttpURLConnection) searchURL.openConnection();

		String searchParameters = getSearchParameters(latitude, longitude, page);

		connection.setRequestProperty("Content-Length", String.valueOf(searchParameters.getBytes().length));
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(searchParameters);
		wr.flush();
		wr.close();

		StringBuilder searchResponse = getResponse(connection);
		
		return parseJsonFromResponse(searchResponse.toString());
	}
	
	protected List<String> parseJsonFromResponse(String searchResponse) {
		
		String jsonStartString = "{ coords : {lon : ";
		String jsonSeparator = ", " + jsonStartString;
		String jsonEndString = "})";
		
		List<String> jsonList = new ArrayList<String>();
		int jsonStartIndex = -1;
		while ((jsonStartIndex = searchResponse.indexOf(jsonStartString, jsonStartIndex+1)) > 0) {
			int jsonEndIndex = searchResponse.indexOf(jsonSeparator, jsonStartIndex);
			if (jsonEndIndex == -1) {
				//This is the end of the list
				jsonEndIndex = searchResponse.indexOf(jsonEndString, jsonStartIndex);
			}
			String jsonString;
			jsonString = searchResponse.substring(jsonStartIndex, jsonEndIndex);
			jsonList.add(jsonString);
		}
		return jsonList;
	}

	private StringBuilder getResponse(HttpURLConnection connection) throws IOException {
		try {
			InputStream in = new BufferedInputStream(connection.getInputStream());
			Reader rd = new BufferedReader(new InputStreamReader(in));
			StringBuilder response = new StringBuilder();
			
			char[] charBuffer = new char[1024 * 4];
			int charsRead;
			while ((charsRead = rd.read(charBuffer)) > 0) {
				response.append(charBuffer, 0, charsRead);
			}
			rd.close();
			return response;
		} finally {
			connection.disconnect();
		}
	}

	private String getSearchParameters(float latitude, float longitude, int page) {
		return "lang=gbr&ie=UTF-8&charset=UTF-8&typeJs=simplePoi&" + 
				"strCoord=" + longitude + "*" + latitude + "&" + 
				"treatment=RestaurantSearch&listCode=GR&empriseH=0&" + 
				"empriseW=0&psize=0&spatialType=concentric&sortBy=distance&" + 
				"page=" + page + "&charset=UTF-8&callBack=JSE.cr.pv[14].cv&version=&" + 
				"from=RESTAURANT_RESULT&lang=int";
	}

	private String getRestaurantDetailsParameters(Restaurant restaurant) {
		return "lang=gbr&ie=UTF-8&charset=UTF-8&typeJs=jsePoi&" + 
				"poiType=true&productId=" + restaurant.productId + "&" + 
				"id=" + restaurant.id + "&zoomLevel=13&" + 
				"treatment=RestaurantSearch&listCode=GR&" + 
				"charset=UTF-8&callBack=JSE.cr.pv[25].cv&lang=eng";
	}

}
