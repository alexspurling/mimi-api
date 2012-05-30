package com.sandstonelabs.mimi;

/**
 * Container class for a restaurant and its cached serialised form.
 * Could potentially store this on the restaurant itself.
 *
 */
public class RestaurantCacheEntry {

	public final Restaurant restaurant;
	public final String restaurantJson;
	
	public RestaurantCacheEntry(Restaurant restaurant, String restaurantJson) {
		this.restaurant = restaurant;
		this.restaurantJson = restaurantJson;
	}
	
}
