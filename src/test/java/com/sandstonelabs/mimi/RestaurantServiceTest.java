package com.sandstonelabs.mimi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

public class RestaurantServiceTest {

	@Test
	public void testCachedRestaurantsAtLocation_emptyCacheFile_returnsEmptyList() throws IOException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheDirectory = clearCacheDirectory();
		
		RestaurantJsonCache restaurantJsonCache = new RestaurantJsonCache(cacheDirectory, jsonParser);
		
		RestaurantService restaurantService = new RestaurantService(null, restaurantJsonCache);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		int page = 1;

		RestaurantResults restaurantResults = restaurantService.getCachedRestaurantsAtLocation(lat, lon, page);
		List<Restaurant> restaurants = restaurantResults.restaurants;
		
		assertTrue(restaurants.isEmpty());
	}
	
	@Test
	public void testCachedRestaurantsAtLocation_returnsFirstPageFromCacheFile() throws IOException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newCacheFileWithExampleData();
		
		RestaurantJsonCache restaurantJsonCache = new RestaurantJsonCache(cacheFile, jsonParser);
		RestaurantService restaurantService = new RestaurantService(null, restaurantJsonCache);
		
		float lat = 51.4915f;
		float lon = -0.1650f;
		int page = 1;
		
		RestaurantResults restaurantResults = restaurantService.getCachedRestaurantsAtLocation(lat, lon, page);
		List<Restaurant> restaurants = restaurantResults.restaurants;
		
		for (Restaurant restaurant : restaurants) {
			System.out.println(restaurant);
		}
		assertEquals(20, restaurants.size());
	}
	
	private File clearCacheDirectory() {
		new File(RestaurantJsonCache.RESTAURANT_CACHE_FILE).delete();
		new File(RestaurantJsonCache.AREA_CACHE_FILE).delete();
		return new File(".");
	}

	private File newCacheFileWithExampleData() throws IOException {
		File cacheDir = clearCacheDirectory();
		File cacheFile = new File(RestaurantJsonCache.RESTAURANT_CACHE_FILE);
		InputStream exampleData = this.getClass().getResourceAsStream("/example-data.txt");
		copyInputStreamToFile(exampleData, cacheFile);
		return cacheDir;
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
