package com.sandstonelabs.mimi;

import java.util.HashMap;
import java.util.Map;

public class RestaurantSearch {

	public static final String BASE_URL = "http://www2.viamichelin.co.uk";
	public static final String SEARCH_URL = "/vmw2/maf/dyn/controller/jseMultiPoiPaginationFinder";
	public static final String SINGLE_POI_URL = "/vmw2/maf/dyn/controller/jseSinglePoiFinder";
	
	public String searchRestaurants(float latitude, float longitude) {
		
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

		return null;
	}
	
	public String getRestaurantDetails(Restaurant restaurant) {
		
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
