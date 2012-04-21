package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Searches for and parses restaurant data 
 *
 */
public class RestaurantService {

	private CachedRestaurantSearch cachedRestaurantSearch;
	private final ApiRestaurantSearch apiRestaurantSearch;
	private final RestaurantJsonParser jsonParser;
	
	public RestaurantService(ApiRestaurantSearch apiRestaurantSearch, CachedRestaurantSearch cachedRestaurantSearch, RestaurantJsonParser jsonParser) {
		this.apiRestaurantSearch = apiRestaurantSearch;
		this.cachedRestaurantSearch = cachedRestaurantSearch;
		this.jsonParser = jsonParser;
	}
	
	/**
	 * Get a list of restaurants centred at the given location. This will first attempt
	 * to retrieve results from the cache, then from the online api. Results are restricted
	 * by the given distance from the location in metres.
	 * @param latitude
	 * @param longitude
	 * @param maxDistance
	 * @return
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	public List<Restaurant> getRestaurantsAtLocation(float latitude, float longitude, int maxDistance) throws JsonParseException, IOException {
		List<Restaurant> cachedRestaurants = cachedRestaurantSearch.getRestaurantsAtLocation(latitude, longitude, maxDistance);
		if (!cachedRestaurants.isEmpty()) {
			return cachedRestaurants;
		}
		List<Restaurant> restaurants = apiRestaurantSearch.searchRestaurants(latitude, longitude);
		cachedRestaurantSearch.storeResultsInCache(restaurants);
		
		return restaurants;
	}
	
}
