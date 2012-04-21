package com.sandstonelabs.mimi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class CachedRestaurantSearch {

	private final File cacheFile;
	private final RestaurantJsonParser jsonParser;

	private Set<Restaurant> cachedRestaurants = new HashSet<Restaurant>();
	private Set<String> cachedRestaurantsJson = new HashSet<String>();

	public CachedRestaurantSearch(File cacheFile, RestaurantJsonParser jsonParser) throws IOException {
		this.cacheFile = cacheFile;
		this.jsonParser = jsonParser;
		loadCache(cacheFile);
	}
	
	public Set<Restaurant> getAllCachedRestaurants() {
		return cachedRestaurants;
	}

	/**
	 * @param latitude
	 *            as a decimal
	 * @param longitude
	 *            as a decimal
	 * @param maxDistance
	 *            as an integer number of metres
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

	public void storeResultsInCache(List<Restaurant> restaurants) throws IOException {
		for (Restaurant restaurant : restaurants) {
			cachedRestaurants.add(restaurant);
			cachedRestaurantsJson.add(jsonParser.convertRestaurantToJsonString(restaurant));
		}
		writeCache();
	}

	private void loadCache(File cacheFile) throws IOException {
		cacheFile.createNewFile(); //Creates a new cache file if the given file does not exist
		LineIterator it = FileUtils.lineIterator(cacheFile, "UTF-8");
		try {
			while (it.hasNext()) {
				String cacheLine = it.next();
				cachedRestaurantsJson.add(cacheLine);
				cachedRestaurants.add(jsonParser.parseRestaurantSearchResultsFromJson(cacheLine));
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}

	private void writeCache() throws IOException {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cacheFile)));
		for (String restaurantJson : cachedRestaurantsJson) {
			writer.println(restaurantJson);
		}
		IOUtils.closeQuietly(writer);
	}

}
