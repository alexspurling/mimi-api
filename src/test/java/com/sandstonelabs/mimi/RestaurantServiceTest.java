package com.sandstonelabs.mimi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;

public class RestaurantServiceTest {

	@Test
	public void testCachedRestaurantsAtLocation_emptyCacheFile_returnsEmptyList() throws IOException, JSONException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newEmptyCacheFile();
		
		RestaurantJsonCache restaurantJsonCache = new RestaurantJsonCache(cacheFile, jsonParser);
		
		RestaurantService restaurantService = new RestaurantService(null, restaurantJsonCache);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		
		List<Restaurant> restaurants = restaurantService.getCachedRestaurantsAtLocation(lat, lon, 300);
		
		assertTrue(restaurants.isEmpty());
	}
	
	@Test
	public void testCachedRestaurantsAtLocation_returnsFilteredListFromCacheFile() throws IOException, JSONException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newCacheFileWithExampleData();
		
		RestaurantJsonCache restaurantJsonCache = new RestaurantJsonCache(cacheFile, jsonParser);
		RestaurantService restaurantService = new RestaurantService(null, restaurantJsonCache);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		int maxDistance = 300;
		
		List<Restaurant> restaurants = restaurantService.getCachedRestaurantsAtLocation(lat, lon, maxDistance);
		
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
		copyInputStreamToFile(exampleData, cacheFile);
		return cacheFile;
	}

	private void copyInputStreamToFile(InputStream exampleData, File cacheFile) throws IOException {
		cacheFile.createNewFile();
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(cacheFile));
		byte[] bytes = new byte[1024];
		int bytesRead;
		while ((bytesRead = exampleData.read(bytes)) > 0) {
			outputStream.write(bytes, 0, bytesRead);
		}
		outputStream.flush();
		outputStream.close();
		exampleData.close();
	}
}
