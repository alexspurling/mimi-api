package com.sandstonelabs.mimi;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class CachedRestaurantSearchTest {

	@Test
	public void testSetUpEmptyCacheFile_returnsEmptyList() throws IOException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newEmptyCacheFile();
		
		CachedRestaurantSearch cachedRestaurantSearch = new CachedRestaurantSearch(cacheFile, jsonParser);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		List<Restaurant> restaurants = cachedRestaurantSearch.getRestaurantsAtLocation(lat, lon, 300);
		
		assertTrue(restaurants.isEmpty());
	}
	
	@Test
	public void testWriteToEmptyCacheFile_newEntriesAreRecorded() throws IOException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newEmptyCacheFile();
		
		CachedRestaurantSearch cachedRestaurantSearch1 = new CachedRestaurantSearch(cacheFile, jsonParser);
		
		Restaurant restaurant1 = new Restaurant.RestaurantBuilder().name("Restaurant 1").build();
		Restaurant restaurant2 = new Restaurant.RestaurantBuilder().name("Restaurant 2").build();
		List<Restaurant> storedRestaurants = Arrays.asList(restaurant1, restaurant2);
		
		cachedRestaurantSearch1.storeResultsInCache(storedRestaurants);
		
		CachedRestaurantSearch cachedRestaurantSearch2 = new CachedRestaurantSearch(cacheFile, jsonParser);
		
		Set<Restaurant> allCachedRestaurants = cachedRestaurantSearch2.getAllCachedRestaurants();
		
		//Make sure that our list of cached restaurants was actually returned
		assertTrue(allCachedRestaurants.containsAll(storedRestaurants));
	}
	
	@Test
	public void testRestaurantsAtLocation_returnsFilteredListFromCacheFile() throws IOException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newCacheFileWithExampleData();
		
		CachedRestaurantSearch cachedRestaurantSearch = new CachedRestaurantSearch(cacheFile, jsonParser);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		List<Restaurant> restaurants = cachedRestaurantSearch.getRestaurantsAtLocation(lat, lon, 300);
		
		for (Restaurant restaurant : restaurants) {
			System.out.println(restaurant);
		}
		assertEquals(7, restaurants.size());
	}

	private File newEmptyCacheFile() throws IOException {
		File cacheFile = new File("test-cache.txt");
		cacheFile.delete(); //Make sure the file does not exist
		return cacheFile;
	}

	private File newCacheFileWithExampleData() throws IOException {
		File cacheFile = new File("test-cache.txt");
		InputStream exampleData = this.getClass().getResourceAsStream("/example-data.txt");
		FileUtils.copyInputStreamToFile(exampleData, cacheFile);
		return cacheFile;
	}

}

