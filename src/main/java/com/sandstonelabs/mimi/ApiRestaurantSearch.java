package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiRestaurantSearch {

	public static final String BASE_URL = "http://www2.viamichelin.co.uk";
	public static final String SEARCH_URL = "/vmw2/maf/dyn/controller/jseMultiPoiPaginationFinder";
	public static final String SINGLE_POI_URL = "/vmw2/maf/dyn/controller/jseSinglePoiFinder";
	
	public List<String> searchRestaurants(float latitude, float longitude) throws IOException {
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("lang", "gbr");
		params.put("ie", "UTF-8");
		params.put("charset", "UTF-8");
		params.put("typeJs", "simplePoi");
		params.put("strCoord", longitude + "*" + latitude);
		params.put("treatment", "RestaurantSearch");
		params.put("listCode", "GR");
		params.put("empriseH", "0");
		params.put("empriseW", "0");
		params.put("psize", "0");
		params.put("spatialType", "concentric");
		params.put("sortBy", "distance");
		params.put("page", "1");
		params.put("charset", "UTF-8");
		params.put("callBack", "JSE.cr.pv[14].cv");
		params.put("version", "");
		params.put("from", "RESTAURANT_RESULT");
		params.put("lang", "int");

		//TODO 
		// - make the API request
		// - clean up the response
		
		/*
		 * 	Map<String,Object> allData = mapper.readValue(jsonData, Map.class);
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		//Contains a list of data for each search result
		List<Map<String,Object>> resultsList = (List<Map<String,Object>>)(List<?>) getListField(allData, "OB"); 
		for (Map<String,Object> result : resultsList) {
		
		String jsonData = null;
		return jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
		 */
		
		return null;
	}
	
	public Restaurant getRestaurantDetails(Restaurant restaurant) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("?lang", "gbr");
		params.put("ie", "UTF-8");
		params.put("charset", "UTF-8");
		params.put("typeJs", "jsePoi");
		params.put("poiType", "true");
		params.put("productId", String.valueOf(restaurant.productId));
		params.put("id", String.valueOf(restaurant.id));
		params.put("zoomLevel", "13");
		params.put("treatment", "RestaurantSearch");
		params.put("listCode", "GR");
		params.put("charset", "UTF-8");
		params.put("callBack", "JSE.cr.pv[25].cv");
		params.put("lang", "eng");
		
		return null;
	}
		
}
