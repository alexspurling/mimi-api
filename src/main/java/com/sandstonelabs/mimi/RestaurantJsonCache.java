package com.sandstonelabs.mimi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class RestaurantJsonCache {

	private static final int maxCacheSize = 100;
	
	private final File cacheFile;
	private final RestaurantJsonParser jsonParser;
	
	private final RestaurantDistanceService restaurantDistanceService = new RestaurantDistanceService();

	//TODO replace these sets with a concurrentlinkedhashmap
	private Set<Restaurant> cachedRestaurants = new HashSet<Restaurant>();
	private Set<String> cachedRestaurantsJson = new HashSet<String>();
	
	//Map of locations and the radius around which we have cached restaurants
	private Map<LatLng, Double> cachedAreas = new HashMap<LatLng, Double>();
	

	public RestaurantJsonCache(File cacheFile, RestaurantJsonParser jsonParser) throws IOException {
		this.cacheFile = cacheFile;
		this.jsonParser = jsonParser;
		loadCache(cacheFile);
	}
	
	public Set<Restaurant> getAllCachedRestaurants() {
		return cachedRestaurants;
	}

	public List<Restaurant> storeResultsInCache(List<String> restaurantJson, float latitude, float longitude) throws IOException {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for (String jsonData : restaurantJson) {
			
			try {
				Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
				restaurants.add(restaurant);
				cachedRestaurants.add(restaurant);
				cachedRestaurantsJson.add(jsonData);
				//TODO Can we assume that the input list of restaurants is ordered
				//And that the last element is the furthest from the search location?
				storeArea(restaurant, latitude, longitude);
			}catch (JSONException e) {
				//TODO log the error somehow
				//Error parsing Json data, skip this string and don't cache it
			}
		}
		writeCache();
		return restaurants;
	}

	private void storeArea(Restaurant restaurant, float latitude, float longitude) {
		LatLng searchLocation = new LatLng(latitude, longitude);
		LatLng restaurantLocation = new LatLng(restaurant.latitude, restaurant.longitude);
		double distanceFromSearch = LatLngTool.distance(searchLocation, restaurantLocation, LengthUnit.METER);
		Double cachedRadius = cachedAreas.get(searchLocation);
		if (cachedRadius == null || cachedRadius < distanceFromSearch) {
			cachedAreas.put(searchLocation, distanceFromSearch);
		}
	}

	private void loadCache(File cacheFile) throws IOException {
		cacheFile.createNewFile(); //Creates a new cache file if the given file does not exist
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile), "UTF-8"));
		try {
			String line;
			int cacheSize = 0;
			while ((line = reader.readLine()) != null && cacheSize < maxCacheSize) {
				cachedRestaurantsJson.add(line);
				try {
					cachedRestaurants.add(jsonParser.parseRestaurantSearchResultsFromJson(line));
					cacheSize++;
				}catch (JSONException e) {
					//TODO log the error somehow
					//Error parsing cache line, continue onto the next
				}
			}
		} finally {
			reader.close();
		}
	}

	private void writeCache() throws IOException {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cacheFile)));
		try {
			int cacheSize = 0;
			for (String restaurantJson : cachedRestaurantsJson) {
				if (cacheSize >= maxCacheSize) {
					break;
				}
				writer.println(restaurantJson);
				cacheSize++;
			}
		}finally{
			writer.close();
		}
	}
	
	public RestaurantResults getRestaurantsAtLocation(float latitude, float longitude, int maxDistance, int maxResults) throws IOException {
		List<Restaurant> restaurantsAtLocation = restaurantDistanceService.filterRestaurantsAtLocation(cachedRestaurants, latitude, longitude, maxDistance, maxResults);
		if (!restaurantsAtLocation.isEmpty()) {
			double searchRadius = restaurantDistanceService.getFurthestRestaurantDistance(latitude, longitude, restaurantsAtLocation);
			return new RestaurantResults(restaurantsAtLocation, isLocationCached(latitude, longitude, searchRadius));
		}
		return new RestaurantResults(restaurantsAtLocation, false);
	}
	
	/**
	 * Checks to see if the given location is within the area that has been cached.
	 * If so, returns all the results from the cache. Otherwise, returns null.
	 */
	public boolean isLocationCached(float latitude, float longitude, double searchRadius) {
		
		LatLng searchLocation = new LatLng(latitude, longitude);
		
		//For each of the cached regions, check to see if our search region
		//lies completely within it. If so then all our results can be safely
		//retrieved from the cache
		for (Entry<LatLng, Double> cachedArea : cachedAreas.entrySet()) {
			LatLng cachedLocation = cachedArea.getKey();
			Double cachedRadius = cachedArea.getValue();
			double distance = LatLngTool.distance(searchLocation, cachedLocation, LengthUnit.METER);
			if (distance + searchRadius <= cachedRadius) {
				return true;
			}
		}
		
		return false;
		
	}

}
