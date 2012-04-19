package com.sandstonelabs.mimi;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandstonelabs.mimi.Restaurant.RestaurantBuilder;

public class RestaurantJsonConverter {

	private final ObjectMapper mapper;

	public RestaurantJsonConverter() {
		mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}
	
	public List<Restaurant> loadRestaurantFromJson(File file) throws JsonParseException, IOException {
		return loadRestaurantFromJson(FileUtils.readFileToString(file));
	}
	
	@SuppressWarnings("unchecked")
	public List<Restaurant> loadRestaurantFromJson(String jsonData) throws JsonParseException, IOException {
		Map<String,Object> allData = mapper.readValue(jsonData, Map.class);
		
		//Contains a list of data for each search result
		List<Map<String,Object>> resultsList = (List<Map<String,Object>>) allData.get("OB"); 
		for (Map<String,Object> result : resultsList) {
			parseRestaurantSearchResult(result);
		}
		//parseRestaurantSearchResult(result);
		
		return null;
	}
	
	private Restaurant parseRestaurantSearchResult(Map<String, Object> result) {
		
		System.out.println("Result: " + result);
		//Parse the expected fields
		int id = getIntField(result, "id");
		int productId = getIntField(result, "productId");
		float latitude = getFloatField(getMapField(result, "coords"), "lat");
		float longitude = getFloatField(getMapField(result, "coords"), "lon");
		
		String name;
		String description;
		String cuisine;
		String foodPrice;
		
		String email;
		String phoneNumber;
		String oneLineAddress;
		String address;
		String city;
		String zipCode;
		String countryCode;
		String country;
		String website;
		
		RestaurantBuilder builder = new Restaurant.RestaurantBuilder();
		
		builder.id(id).
		productId(productId).
		latitude(latitude).
		longitude(longitude).		
		name(name).
		description(description).
		cuisine(cuisine).
		foodPrice(foodPrice).		
		email(email).
		phoneNumber(phoneNumber).
		oneLineAddress(oneLineAddress).
		address(address).
		city(city).
		zipCode(zipCode).
		countryCode(countryCode).
		country(country).
		website(website);
		
		return builder.build();
	}

	private int getIntField(Map<String, Object> result, String key) {
		try {
			String value = (String) result.get(key);
			return Integer.parseInt(value);
		}catch(ClassCastException e) {
			return 0;
		}
	}

	private float getFloatField(Map<String, Object> result, String key) {
		try {
			String value = (String) result.get(key);
			return Float.parseFloat(value);
		}catch(ClassCastException e) {
			return 0;
		}
	}

	private Map<String, Object> getMapField(Map<String, Object> result, String key) {
		try {
			return (Map<String, Object>) result.get(key);
		}catch(ClassCastException e) {
			return Collections.emptyMap();
		}
	}
}
