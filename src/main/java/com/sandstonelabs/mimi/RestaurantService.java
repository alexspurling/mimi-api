package com.sandstonelabs.mimi;

import java.util.List;

/**
 * Searches for and parses restaurant data 
 *
 */
public class RestaurantService {

	private CachedRestaurantSearch cachedRestaurantSearch;
	private final ApiRestaurantSearch apiRestaurantSearch;
	
	public RestaurantService(ApiRestaurantSearch apiRestaurantSearch, CachedRestaurantSearch cachedRestaurantSearch) {
		this.apiRestaurantSearch = apiRestaurantSearch;
		this.cachedRestaurantSearch = cachedRestaurantSearch;
	}
	
	/**
	 * Get a list of restaurants centred at the given location. This will first attempt
	 * to retrieve results from the cache, then from the online api. Results are restricted
	 * by the given distance from the location in metres.
	 * @param latitude
	 * @param longitude
	 * @param maxDistance
	 * @return
	 */
	public List<Restaurant> getRestaurantsAtLocation(float latitude, float longitude, int maxDistance) {
		return cachedRestaurantSearch.getRestaurantsAtLocation(latitude, longitude, maxDistance);
	}
	
}
