package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.List;

/**
 * Searches for and parses restaurant data 
 *
 */
public class RestaurantService {

	private RestaurantJsonCache restaurantJsonCache;
	private final ApiRestaurantSearch apiRestaurantSearch;
	
	public RestaurantService(ApiRestaurantSearch apiRestaurantSearch, RestaurantJsonCache restaurantJsonCache) {
		this.apiRestaurantSearch = apiRestaurantSearch;
		this.restaurantJsonCache = restaurantJsonCache;
	}
	
	public RestaurantResults getCachedRestaurantsAtLocation(float latitude, float longitude, int startIndex, int numResults) throws IOException {
		return restaurantJsonCache.getRestaurantsAtLocation(latitude, longitude, startIndex, numResults);
	}
	
	public List<Restaurant> getApiRestaurantsAtLocation(float latitude, float longitude, int startIndex) throws IOException {
		List<String> restaurantSearchJson = apiRestaurantSearch.searchRestaurants(latitude, longitude, startIndex);
		return restaurantJsonCache.storeResultsInCache(restaurantSearchJson, latitude, longitude);
	}
	
}
