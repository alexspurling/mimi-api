package com.sandstonelabs.mimi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

public class RestaurantJsonCache {

	private final File cacheFile;
	private final RestaurantJsonParser jsonParser;

	//TODO replace these sets with a concurrentlinkedhashmap
	private Set<Restaurant> cachedRestaurants = new HashSet<Restaurant>();
	private Set<String> cachedRestaurantsJson = new HashSet<String>();

	public RestaurantJsonCache(File cacheFile, RestaurantJsonParser jsonParser) throws IOException, JSONException {
		this.cacheFile = cacheFile;
		this.jsonParser = jsonParser;
		loadCache(cacheFile);
	}
	
	public Set<Restaurant> getAllCachedRestaurants() {
		return cachedRestaurants;
	}

	public void storeResultsInCache(List<String> restaurantJson) throws IOException, JSONException {
		for (String jsonData : restaurantJson) {
			cachedRestaurants.add(jsonParser.parseRestaurantSearchResultsFromJson(jsonData));
			cachedRestaurantsJson.add(jsonData);
		}
		writeCache();
	}

	private void loadCache(File cacheFile) throws IOException, JSONException {
		cacheFile.createNewFile(); //Creates a new cache file if the given file does not exist
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile), "UTF-8"));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				cachedRestaurantsJson.add(line);
				cachedRestaurants.add(jsonParser.parseRestaurantSearchResultsFromJson(line));
			}
		} finally {
			reader.close();
		}
	}

	private void writeCache() throws IOException {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cacheFile)));
		try {
			for (String restaurantJson : cachedRestaurantsJson) {
				writer.println(restaurantJson);
			}
		}finally{
			writer.close();
		}
	}

}
