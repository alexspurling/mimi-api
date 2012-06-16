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
	
	public List<Restaurant> filterRestaurantsAtLocation(Iterator<Restaurant> restaurantList, float latitude, float longitude, int maxDistance, int maxResults) {
		//Get all the results from the cache and filter by location
		final LatLng searchLocation = new LatLng(latitude, longitude);
		
		Map<Restaurant, Integer> restaurantDistanceMap = new HashMap<Restaurant, Integer>();
		
		Restaurant restaurant;
		while (restaurantList.hasNext() && (restaurant = restaurantList.next()) != null) {
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
	
	private static final Comparator<Entry<Restaurant, Integer>> restaurantDistanceComparator = new Comparator<Entry<Restaurant, Integer>>() {
		@Override
		public int compare(Entry<Restaurant, Integer> o1, Entry<Restaurant, Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
}
