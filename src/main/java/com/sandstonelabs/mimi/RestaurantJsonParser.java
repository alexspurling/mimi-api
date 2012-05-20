package com.sandstonelabs.mimi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sandstonelabs.mimi.Restaurant.RestaurantBuilder;

public class RestaurantJsonParser {
	
	private List<String> errors = new ArrayList<String>();
	
	public Restaurant parseRestaurantSearchResultsFromJson(String jsonData) throws IOException, JSONException {
		return parseRestaurantSearchResult(new JSONObject(jsonData));
	}
	
	private Restaurant parseRestaurantSearchResult(JSONObject result) throws JSONException {
		
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

	private String getField(JSONObject result, String key) throws JSONException {
		try {
			return (String) result.get(key);
		}catch(ClassCastException e) {
			recordError("Field not found: " + key);
			return null;
		}
	}

	private String getListItem(JSONArray listField, int i) throws JSONException {
		if (i < listField.length()) {
			return (String)listField.get(i);
		}
		recordError("Index not found in list. Index: " + i + ", list size: " + listField.length());
		return null;
	}

	private JSONArray getListField(JSONObject result, String key) throws JSONException {
		try {
			if (result.get(key) != null) {
				return (JSONArray) result.get(key);
			}
		}catch(ClassCastException e) {
			recordError("Expected list field for key: " + key + ", but got: " + result.get(key));
		}
		return new JSONArray();
	}

	private int getIntField(JSONObject result, String key) throws JSONException {
		Object value = result.get(key);
		if (value instanceof Integer) {
			return (Integer)value;
		}else if (value instanceof Long) {
			return ((Long) value).intValue();
		}else if (value instanceof String) {
			return Integer.parseInt((String)value);
		}
		recordError("Int field not found: " + key);
		return 0;
	}

	private float getFloatField(JSONObject result, String key) throws JSONException {
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

	private JSONObject getMapField(JSONObject result, String key) throws JSONException {
		try {
			if (result.get(key) != null) {
				return(JSONObject) result.get(key);
			}
		}catch(ClassCastException e) {
			recordError("Expected map field for key: " + key + ", but got: " + result.get(key));
		}
		return new JSONObject();
	}
}
