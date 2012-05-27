package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

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
	
	public List<Restaurant> getCachedRestaurantsAtLocation(float latitude, float longitude, int maxDistance, int maxResults) throws IOException {
		Set<Restaurant> cachedRestaurants = restaurantJsonCache.getAllCachedRestaurants();
		return getRestaurantsAtLocation(cachedRestaurants, latitude, longitude, maxDistance, maxResults);
	}
	
	public List<Restaurant> getApiRestaurantsAtLocation(float latitude, float longitude, int page) throws IOException {
		List<String> restaurantSearchJson = apiRestaurantSearch.searchRestaurants(latitude, longitude, page);
		return restaurantJsonCache.storeResultsInCache(restaurantSearchJson);
	}
	
	private static final Comparator<Entry<Restaurant, Integer>> restaurantDistanceComparator = new Comparator<Entry<Restaurant, Integer>>() {
		@Override
		public int compare(Entry<Restaurant, Integer> o1, Entry<Restaurant, Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
	
	private List<Restaurant> getRestaurantsAtLocation(Collection<Restaurant> restaurants, float latitude, float longitude, int maxDistance, int maxResults) {
		//Get all the results from the cache and filter by location
		final LatLng searchLocation = new LatLng(latitude, longitude);
		
		Map<Restaurant, Integer> restaurantDistanceMap = new HashMap<Restaurant, Integer>();
		
		for (Restaurant restaurant : restaurants) {
			LatLng restaurantLocation = new LatLng(restaurant.latitude, restaurant.longitude);
			double distance = LatLngTool.distance(searchLocation, restaurantLocation, LengthUnit.METER);
			if (distance <= maxDistance) {
				restaurantDistanceMap.put(restaurant, (int)distance);
			}
		}
		
		if (restaurantDistanceMap.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Entry<Restaurant, Integer>> restaurantEntryList = new ArrayList<Entry<Restaurant, Integer>>(restaurantDistanceMap.entrySet());
		Collections.sort(restaurantEntryList, restaurantDistanceComparator);
		
		int count = 0;
		List<Restaurant> sortedRestaurants = new ArrayList<Restaurant>();
		for (Entry<Restaurant, Integer> restaurantEntry : restaurantEntryList) {
			if (count >= maxResults) {
				break;
			}
			sortedRestaurants.add(restaurantEntry.getKey());
			count++;
		}
		
		return sortedRestaurants;
	}
	
}
