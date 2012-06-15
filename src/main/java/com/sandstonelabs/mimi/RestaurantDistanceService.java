package com.sandstonelabs.mimi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class RestaurantDistanceService {

	public double getFurthestRestaurantDistance(float latitude, float longitude, List<Restaurant> restaurantsAtLocation) {
		final LatLng searchLocation = new LatLng(latitude, longitude);
		//Assume the restaurants are ordered nearest to farthest
		Restaurant furthestRestaurant = restaurantsAtLocation.get(restaurantsAtLocation.size()-1);
		LatLng furthestRestaurantLocation = new LatLng(furthestRestaurant.latitude, furthestRestaurant.longitude);
		double searchRadius = LatLngTool.distance(searchLocation, furthestRestaurantLocation, LengthUnit.METER);
		return searchRadius;
	}
	
	public List<Restaurant> filterRestaurantsAtLocation(Collection<Restaurant> restaurants, float latitude, float longitude, int maxDistance, int maxResults) {
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
	
	private static final Comparator<Entry<Restaurant, Integer>> restaurantDistanceComparator = new Comparator<Entry<Restaurant, Integer>>() {
		@Override
		public int compare(Entry<Restaurant, Integer> o1, Entry<Restaurant, Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
}
