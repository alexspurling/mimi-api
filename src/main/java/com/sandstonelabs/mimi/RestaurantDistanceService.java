package com.sandstonelabs.mimi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class RestaurantDistanceService {
	
	//This should really be passed in by the client but we're hardcoding it here for simplicity
	private static final int RESULTS_PER_PAGE = 20;
	
	Logger log = LoggerFactory.getLogger(RestaurantDistanceService.class);

	public double getFurthestRestaurantDistance(float latitude, float longitude, List<Restaurant> restaurantsAtLocation) {
		final LatLng searchLocation = new LatLng(latitude, longitude);
		
		log.info("Getting furthest restaurant from (" + latitude + ", " + longitude + ")");
		
		//Assume the restaurants are ordered nearest to farthest
		Restaurant furthestRestaurant = restaurantsAtLocation.get(restaurantsAtLocation.size()-1);
		LatLng furthestRestaurantLocation = new LatLng(furthestRestaurant.latitude, furthestRestaurant.longitude);
		double searchRadius = LatLngTool.distance(searchLocation, furthestRestaurantLocation, LengthUnit.METER);

		log.info("Furthest restaurant distance " + searchRadius + ": " + furthestRestaurant);
		
		return searchRadius;
	}
	
	public List<Restaurant> filterRestaurantsAtLocation(Iterator<Restaurant> restaurantList, float latitude, float longitude, int page) {
		//Get all the results from the cache and filter by location
		final LatLng searchLocation = new LatLng(latitude, longitude);
		
		Map<Restaurant, Integer> restaurantDistanceMap = new HashMap<Restaurant, Integer>();
		
		Restaurant restaurant;
		while (restaurantList.hasNext() && (restaurant = restaurantList.next()) != null) {
			LatLng restaurantLocation = new LatLng(restaurant.latitude, restaurant.longitude);
			double distance = LatLngTool.distance(searchLocation, restaurantLocation, LengthUnit.METER);
			restaurantDistanceMap.put(restaurant, (int)distance);
		}
		
		if (restaurantDistanceMap.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Entry<Restaurant, Integer>> restaurantEntryList = new ArrayList<Entry<Restaurant, Integer>>(restaurantDistanceMap.entrySet());
		Collections.sort(restaurantEntryList, restaurantDistanceComparator);
		
		//The list is now sorted by distance, make sure we return just the 20 needed for the given page
		int startIndex = Math.min((page - 1) * RESULTS_PER_PAGE, restaurantEntryList.size());
		int endIndex = Math.min(page * RESULTS_PER_PAGE, restaurantEntryList.size());
		
		List<Restaurant> sortedRestaurants = new ArrayList<Restaurant>();
		for (int i = startIndex; i < endIndex; i++) {
			Entry<Restaurant, Integer> restaurantEntry = restaurantEntryList.get(i);
			sortedRestaurants.add(restaurantEntry.getKey());
		}
		
		return sortedRestaurants;
	}
	
	private static final Comparator<Entry<Restaurant, Integer>> restaurantDistanceComparator = new Comparator<Entry<Restaurant, Integer>>() {
		@Override
		public int compare(Entry<Restaurant, Integer> o1, Entry<Restaurant, Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
}
