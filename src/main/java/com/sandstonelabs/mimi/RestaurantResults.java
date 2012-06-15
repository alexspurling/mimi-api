package com.sandstonelabs.mimi;

import java.util.List;

public class RestaurantResults {

	public final List<Restaurant> restaurants;
	public final boolean fullResults;
	
	public RestaurantResults(List<Restaurant> restaurants, boolean exactResults) {
		this.restaurants = restaurants;
		this.fullResults = exactResults;
	}
	
}
