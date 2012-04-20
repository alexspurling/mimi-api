package com.sandstonelabs.mimi;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;


public class RestaurantJsonConverterTest {

	@Test
	public void testLoadRestaurantFromJson() throws JsonParseException, IOException {
		RestaurantJsonConverter jsonConverter = new RestaurantJsonConverter();
		
		jsonConverter.loadRestaurantFromJson(new File("response-clean.txt"));
	}

}
