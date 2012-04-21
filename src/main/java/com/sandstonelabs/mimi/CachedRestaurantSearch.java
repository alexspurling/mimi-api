package com.sandstonelabs.mimi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class CachedRestaurantSearch {
	
	private final File cacheFile;
	private final RestaurantJsonParser jsonParser;
	
	private List<Restaurant> cachedRestaurants;
	
	public CachedRestaurantSearch(File cacheFile, RestaurantJsonParser jsonParser) throws JsonParseException, IOException {
		this.cacheFile = cacheFile;
		this.jsonParser = jsonParser;
		loadCache(cacheFile);
	}

	/**
	 * @param latitude as a decimal
	 * @param longitude as a decimal
	 * @param maxDistance as an integer number of metres
	 * @return
	 */
	public List<Restaurant> getRestaurantsAtLocation(float latitude, float longitude, int maxDistance) {
		LatLng searchLocation = new LatLng(latitude, longitude);
		
		List<Restaurant> nearbyRestaurants = new ArrayList<Restaurant>();
		for (Restaurant restaurant : cachedRestaurants) {
			LatLng restaurantLocation = new LatLng(restaurant.latitude, restaurant.longitude);
			double distance = LatLngTool.distance(searchLocation, restaurantLocation, LengthUnit.METER);
			if (distance <= maxDistance) {
				nearbyRestaurants.add(restaurant);
			}
		}
		return nearbyRestaurants;
	}
	
	private void loadCache(File cacheFile) throws JsonParseException, IOException {
		cachedRestaurants = jsonParser.parseRestaurantSearchResultsFromJson(cacheFile);
	}

}
