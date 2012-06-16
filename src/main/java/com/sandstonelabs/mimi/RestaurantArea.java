package com.sandstonelabs.mimi;

import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

public class RestaurantArea {

	public final LatLng location;
	public final double radius;
	public final List<Integer> restaurantIds;
	
	public RestaurantArea(LatLng location, double radius, List<Integer> restaurantIds) {
		this.location = location;
		this.radius = radius;
		this.restaurantIds = restaurantIds;
	}
	
	private static final char separator = '|';
	
	@Override
	public String toString() {
		StringBuilder restaurantAreaString = new StringBuilder();
		restaurantAreaString.append(location.getLatitude());
		restaurantAreaString.append(separator);
		restaurantAreaString.append(location.getLongitude());
		restaurantAreaString.append(separator);
		restaurantAreaString.append(radius);
		for (Integer restaurantId : restaurantIds) {
			restaurantAreaString.append(separator);
			restaurantAreaString.append(restaurantId);
		}
		return restaurantAreaString.toString();
	}
	
	public static RestaurantArea fromString(String restaurantAreaString) {
		String[] sections = restaurantAreaString.split("[" + String.valueOf(separator) + "]");
		
		LatLng location = new LatLng(Double.parseDouble(sections[0]), Double.parseDouble(sections[1]));
		double radius = Double.parseDouble(sections[2]);
		
		List<Integer> restaurantIds = new ArrayList<Integer>();
		for (int i = 3; i < sections.length; i++) {
			restaurantIds.add(Integer.parseInt(sections[i]));
		}
		
		return new RestaurantArea(location, radius, restaurantIds);
	}
}
