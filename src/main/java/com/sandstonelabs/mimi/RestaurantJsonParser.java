package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandstonelabs.mimi.Restaurant.RestaurantBuilder;

@SuppressWarnings("unchecked")
public class RestaurantJsonParser {

	private final ObjectMapper mapper;
	
	private List<String> errors = new ArrayList<String>();
	
	public RestaurantJsonParser() {
		mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}
	
	public Restaurant parseRestaurantSearchResultsFromJson(String jsonData) throws JsonParseException, IOException {
		return parseRestaurantSearchResult(mapper.readValue(jsonData, Map.class));
	}
	
	private Restaurant parseRestaurantSearchResult(Map<String, Object> result) {
		
		//Parse the expected fields
		int id = getIntField(result, "id");
		int productId = getIntField(result, "productId");
		float latitude = getFloatField(getMapField(result, "coords"), "lat");
		float longitude = getFloatField(getMapField(result, "coords"), "lon");
		
		String name = getField(result, "name");
		String description = getListItem(getListField(result, "vE"), 7);
		String cuisine = getListItem(getListField(result, "vE"), 6);
		String foodPrice = getListItem(getListField(result, "vE"), 4);
		
		String email = getField(result, "Ik");
		String phoneNumber = getField(result, "telephone");
		String oneLineAddress = getField(result, "vX");
		String address = getField(result, "address");
		String city = getField(result, "city");
		String zipCode = getField(result, "zipCode");
		String countryCode = getField(result, "sO");
		String country = getField(result, "countryLabel");
		String website = getField(result, "aac");
		
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

	private void recordError(String string) {
		errors.add(string);
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public String convertRestaurantToJsonString(Restaurant restaurant) throws JsonParseException, IOException {
		return mapper.writeValueAsString(restaurant);
	}

	private String getField(Map<String, Object> result, String key) {
		try {
			return (String) result.get(key);
		}catch(ClassCastException e) {
			recordError("Field not found: " + key);
			return null;
		}
	}

	private String getListItem(List<Object> listField, int i) {
		if (i < listField.size()) {
			return (String)listField.get(i);
		}
		recordError("Index not found in list. Index: " + i + ", list size: " + listField.size());
		return null;
	}

	private List<Object> getListField(Map<String, Object> result, String key) {
		try {
			return (List<Object>) result.get(key);
		}catch(ClassCastException e) {
			recordError("List field not found: " + key);
			return Collections.emptyList();
		}
	}

	private int getIntField(Map<String, Object> result, String key) {
		Object value = result.get(key);
		if (value instanceof Integer) {
			return (Integer)value;
		}else if (value instanceof String) {
			return Integer.parseInt((String)value);
		}
		recordError("Int field not found: " + key);
		return 0;
	}

	private float getFloatField(Map<String, Object> result, String key) {
		Object value = result.get(key);
		if (value instanceof Double) {
			return ((Double) value).floatValue();
		}else if (value instanceof Float) {
			return (Float)value;
		}else if (value instanceof String) {
			return Float.parseFloat((String)value);
		}
		recordError("Float field not found: " + key);
		return 0.0f;
	}

	private Map<String, Object> getMapField(Map<String, Object> result, String key) {
		try {
			return (Map<String, Object>) result.get(key);
		}catch(ClassCastException e) {
			recordError("Map field not found: " + key);
			return Collections.emptyMap();
		}
	}
}
