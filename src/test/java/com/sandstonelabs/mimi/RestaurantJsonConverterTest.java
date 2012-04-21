package com.sandstonelabs.mimi;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;


public class RestaurantJsonConverterTest {

	@Test
	public void testLoadRestaurantFromJson() throws JsonParseException, IOException {
		RestaurantJsonParser jsonConverter = new RestaurantJsonParser();
		
		List<Restaurant> restaurants = jsonConverter.parseRestaurantSearchResultsFromJson(new File("response-clean.txt"));
		
		assertFalse(restaurants.isEmpty());
		assertTrue(jsonConverter.getErrors().isEmpty());
	}

}
