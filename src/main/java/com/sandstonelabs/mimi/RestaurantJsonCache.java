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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class RestaurantJsonCache {
	
	private static final Logger log = LoggerFactory.getLogger(RestaurantJsonCache.class);

	private static final int MAX_CACHE_SIZE = 100;

	public static final String RESTAURANT_CACHE_FILE = "restaurant_cache.txt";
	public static final String AREA_CACHE_FILE = "restaurant_area_cache.txt";
	
	private final File restaurantCacheFile;
	private final File areaCacheFile;
	
	private final RestaurantJsonParser jsonParser;
	
	private final RestaurantDistanceService restaurantDistanceService = new RestaurantDistanceService();

	private ConcurrentMap<Integer, RestaurantCacheEntry> cachedRestaurants;
	private ConcurrentMap<LatLng, RestaurantArea> cachedAreas = new ConcurrentHashMap<LatLng, RestaurantArea>();

	private EvictionListener<Integer, RestaurantCacheEntry> cacheEvictionListener = new EvictionListener<Integer, RestaurantCacheEntry>() {
		@Override
		public void onEviction(Integer restaurantId, RestaurantCacheEntry cacheEntry) {
			log.info("Evicting restaurant with id " + restaurantId + ": " + cacheEntry.restaurant);
			expireAreaCacheForRestaurant(restaurantId);
		}
	};
	
	public RestaurantJsonCache(File cacheLocation, RestaurantJsonParser jsonParser) throws IOException {
		this.restaurantCacheFile = new File(cacheLocation, RESTAURANT_CACHE_FILE);
		this.areaCacheFile = new File(cacheLocation, AREA_CACHE_FILE);
		this.jsonParser = jsonParser;
		loadCache();
	}

	public List<Restaurant> storeResultsInCache(List<String> restaurantJson, float latitude, float longitude) throws IOException {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		LatLng searchLocation = new LatLng(latitude, longitude);
		
		log.info("Storing " + restaurantJson.size() + " results in cache. Current cache size is " + cachedRestaurants.size());
		
		double maxRestaurantDistance = 0;
		for (String jsonData : restaurantJson) {
			
			try {
				Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
				restaurants.add(restaurant);
				
				if (cachedRestaurants.get(restaurant.id) == null) {
					RestaurantCacheEntry restaurantCacheEntry = new RestaurantCacheEntry(restaurant, jsonData);
					cachedRestaurants.put(restaurant.id, restaurantCacheEntry);
					log.info("Storing restaurant: " + restaurant.name);
				}else{
					log.info("Already stored restaurant: " + restaurant.name);
				}
				
				//TODO can we just assume the last restaurant is the most distant?
				maxRestaurantDistance = maxDistance(searchLocation, restaurant, maxRestaurantDistance);
				
			}catch (JSONException e) {
				//TODO log the error somehow
				//Error parsing Json data, skip this string and don't cache it
			}
		}
		storeArea(restaurants, searchLocation, maxRestaurantDistance);
		
		writeCache();
		return restaurants;
	}

	private double maxDistance(LatLng searchLocation, Restaurant restaurant, double maxRestaurantDistance) {
		LatLng restaurantLocation = new LatLng(restaurant.latitude, restaurant.longitude);
		double restaurantDistance = LatLngTool.distance(searchLocation, restaurantLocation, LengthUnit.METER);
		if (restaurantDistance > maxRestaurantDistance) {
			return restaurantDistance;
		}
		return maxRestaurantDistance;
	}

	private void storeArea(List<Restaurant> restaurants, LatLng searchLocation, double maxRestaurantDistance) {
		RestaurantArea cachedArea = cachedAreas.get(searchLocation);
		if (cachedArea != null) {
			//Only add new restaurant ids if the area radius has increased
			if (maxRestaurantDistance > cachedArea.radius) {
				//Add the new restaurant ids to this area
				for (Restaurant restaurant : restaurants) {
					cachedArea.restaurantIds.add(restaurant.id);
				}
				cachedArea.radius = maxRestaurantDistance;
				log.info("Expanded area: " + searchLocation + ", radius: " + cachedArea.radius + ", ids: " + cachedArea.restaurantIds);
			}
		}else{
			Set<Integer> restaurantIds = new HashSet<Integer>();
			for (Restaurant restaurant : restaurants) {
				restaurantIds.add(restaurant.id);
			}
			cachedArea = new RestaurantArea(searchLocation, maxRestaurantDistance, restaurantIds);
			cachedAreas.put(searchLocation, cachedArea);
			log.info("Storing new area: " + searchLocation + ", radius: " + cachedArea.radius + ", ids: " + cachedArea.restaurantIds);
		}
	}
	
	private void expireAreaCacheForRestaurant(Integer restaurantId) {
		List<LatLng> areasToExpire = new ArrayList<LatLng>();
		for (Entry<LatLng, RestaurantArea> area : cachedAreas.entrySet()) {
			if (area.getValue().restaurantIds.contains(restaurantId)) {
				areasToExpire.add(area.getKey());
			}
		}
		//TODO change the area data structure to store restaurants by distance
		//Then only remove the given restaurant and those more distant than it
		//from the search location
		for (LatLng areaKey : areasToExpire) {
			RestaurantArea removedArea = cachedAreas.remove(areaKey);
			log.info("Removed area " + areaKey + " with " + removedArea.restaurantIds.size() + " cached restaurants");
		}
	}

	private void loadCache() throws IOException {
		loadRestaurantCache();
		loadAreaCache();
	}

	private void loadRestaurantCache() throws IOException {
		//Set up the cache data structure
		cachedRestaurants = new ConcurrentLinkedHashMap.Builder<Integer, RestaurantCacheEntry>()
			    .maximumWeightedCapacity(MAX_CACHE_SIZE)
			    .listener(cacheEvictionListener)
			    .build();
		
		restaurantCacheFile.createNewFile(); //Creates a new cache file if the given file does not exist
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(restaurantCacheFile), "UTF-8"));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				try {
					String restaurantJson = line;
					Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(line);
					RestaurantCacheEntry restaurantCacheEntry = new RestaurantCacheEntry(restaurant, restaurantJson);
					cachedRestaurants.put(restaurant.id, restaurantCacheEntry);
				}catch (JSONException e) {
					//TODO log the error somehow
					//Error parsing cache line, continue onto the next
				}
			}
		} finally {
			reader.close();
		}
	}

	private void loadAreaCache() throws IOException {
		areaCacheFile.createNewFile(); //Creates a new cache file if the given file does not exist
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(areaCacheFile), "UTF-8"));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				RestaurantArea restaurantArea = RestaurantArea.fromString(line);
				cachedAreas.put(restaurantArea.location, restaurantArea);
			}
		} finally {
			reader.close();
		}
	}

	private void writeCache() throws IOException {
		writeRestaurantCache();
		writeAreaCache();
	}
	
	private void writeRestaurantCache() throws IOException {
		log.info("Writing cache to disk current size is " + cachedRestaurants.size());
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(restaurantCacheFile)));
		try {
			for (RestaurantCacheEntry restaurantCacheEntry : cachedRestaurants.values()) {
				writer.println(restaurantCacheEntry.restaurantJson);
			}
		}finally{
			writer.close();
		}
	}
	
	private void writeAreaCache() throws IOException {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(areaCacheFile)));
		try {
			for (RestaurantArea restaurantArea : cachedAreas.values()) {
				writer.println(restaurantArea.toString());
			}
		}finally{
			writer.close();
		}
	}
	
	public RestaurantResults getRestaurantsAtLocation(float latitude, float longitude, int page) throws IOException {
		Iterator<Restaurant> restaurantList = getCachedRestaurantsIteractor();
		List<Restaurant> restaurantsAtLocation = restaurantDistanceService.filterRestaurantsAtLocation(restaurantList, latitude, longitude, page);
		
		log.info("Found " + restaurantsAtLocation.size() + " restaurants for location (" + latitude + ", " + longitude + ")");
		if (!restaurantsAtLocation.isEmpty()) {
			double searchRadius = restaurantDistanceService.getFurthestRestaurantDistance(latitude, longitude, restaurantsAtLocation);
			return new RestaurantResults(restaurantsAtLocation, isLocationCached(latitude, longitude, searchRadius));
		}
		
		return new RestaurantResults(restaurantsAtLocation, false);
	}
	
	//TODO not sure if this is a good way to convert a list of RestaurantCacheEntries into a list of Restaurants
	private Iterator<Restaurant> getCachedRestaurantsIteractor() {
		
		final Iterator<RestaurantCacheEntry> cachedRestaurantsIteractor = cachedRestaurants.values().iterator();
		
		return new Iterator<Restaurant>() {
			@Override
			public boolean hasNext() {
				return cachedRestaurantsIteractor.hasNext();
			}
			@Override
			public Restaurant next() {
				return cachedRestaurantsIteractor.next().restaurant;
			}
			@Override
			public void remove() {
				cachedRestaurantsIteractor.remove();
			}
		};
	}

	/**
	 * Checks to see if the given location is within the area that has been cached.
	 * If so, returns all the results from the cache. Otherwise, returns null.
	 */
	public boolean isLocationCached(float latitude, float longitude, double searchRadius) {
		
		LatLng searchLocation = new LatLng(latitude, longitude);
		
		log.info("Checking cached areas for location " + searchLocation + " within raduis " + searchRadius);
		
		log.info("Areas cached: " + cachedAreas.size());
		
		//For each of the cached regions, check to see if our search region
		//lies completely within it. If so then all our results can be safely
		//retrieved from the cache
		for (Entry<LatLng, RestaurantArea> cachedArea : cachedAreas.entrySet()) {
			LatLng cachedLocation = cachedArea.getKey();
			double cachedRadius = cachedArea.getValue().radius;
			double distance = LatLngTool.distance(searchLocation, cachedLocation, LengthUnit.METER);

			log.info("Area location: " + cachedLocation + ", radius: " + cachedRadius + ", distance: " + distance + ", search radius + distance: " + (distance + searchRadius));
			
			if (distance + searchRadius <= cachedRadius) {
				log.info("Cached area found at location " + cachedLocation);
				return true;
			}
		}
		
		return false;
		
	}

}
