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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

public class RestaurantJsonCache {

	private static final int maxCacheSize = 100;
	
	private final File cacheFile;
	private final RestaurantJsonParser jsonParser;

	//TODO replace these sets with a concurrentlinkedhashmap
	private Set<Restaurant> cachedRestaurants = new HashSet<Restaurant>();
	private Set<String> cachedRestaurantsJson = new HashSet<String>();

	public RestaurantJsonCache(File cacheFile, RestaurantJsonParser jsonParser) throws IOException {
		this.cacheFile = cacheFile;
		this.jsonParser = jsonParser;
		loadCache(cacheFile);
	}
	
	public Set<Restaurant> getAllCachedRestaurants() {
		return cachedRestaurants;
	}

	public List<Restaurant> storeResultsInCache(List<String> restaurantJson) throws IOException {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for (String jsonData : restaurantJson) {
			
			try {
				Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
				restaurants.add(restaurant);
				cachedRestaurants.add(restaurant);
				cachedRestaurantsJson.add(jsonData);
			}catch (JSONException e) {
				//TODO log the error somehow
				//Error parsing Json data, skip this string and don't cache it
			}
		}
		writeCache();
		return restaurants;
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

}
