package com.sandstonelabs.mimi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sandstonelabs.mimi.Restaurant.RestaurantBuilder;
import com.sandstonelabs.mimi.RestaurantRating.RatingType;

public class RestaurantJsonParser {
	
	private List<String> errors = new ArrayList<String>();
	
	public Restaurant parseRestaurantSearchResultsFromJson(String jsonData) throws JSONException {
		return parseRestaurantSearchResult(new JSONObject(jsonData));
	}
	
	private Restaurant parseRestaurantSearchResult(JSONObject result) {
		
		//Parse the expected fields
		int id = getIntField(result, "id");
		int productId = getIntField(result, "productId");
		float latitude = getFloatField(getMapField(result, "coords"), "lat");
		float longitude = getFloatField(getMapField(result, "coords"), "lon");
		
		String name = getField(result, "name");
		String description = getListItem(getListField(result, "vE"), 7);
		String cuisine = capitalise(getListItem(getListField(result, "vE"), 6));
		String foodPrice = getListItem(getListField(result, "vE"), 4);
		
		JSONArray ratingsList = getListField(result, "CY");
		String comfortRatingImage = getListItem(ratingsList, 2);
		String comfortRatingDescription = getListItem(ratingsList, 3);
		RestaurantRating comfortRating = getRestaurantRating(comfortRatingImage, comfortRatingDescription);
		
		String qualityRatingImage = getListItem(ratingsList, 4);
		String qualityRatingDescription = getListItem(ratingsList, 5);
		RestaurantRating qualityRating = getRestaurantRating(qualityRatingImage, qualityRatingDescription);
		
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
		comfortRating(comfortRating).
		qualityRating(qualityRating).
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
	
	/** Capitalise the first letter of the given string */
	private String capitalise(String string) {
		if (string != null && !string.isEmpty()) {
			char firstLetter = string.charAt(0);
			if (Character.isLowerCase(firstLetter)) {
				return Character.toUpperCase(firstLetter) + string.substring(1);
			}
		}
		return string;
	}

	private RestaurantRating getRestaurantRating(String ratingImage, String ratingDescription) {
		String ratingImageFile = ratingImage.replaceFirst(".*/(.*)", "$1");
		if ("1_12.gif".equals(ratingImageFile)) {
			return new RestaurantRating(1, RatingType.COMFORTABLE, ratingDescription);
		}else if ("1_13.gif".equals(ratingImageFile)) {
			return new RestaurantRating(2, RatingType.COMFORTABLE, ratingDescription);
		}else if ("1_14.gif".equals(ratingImageFile)) {
			return new RestaurantRating(3, RatingType.COMFORTABLE, ratingDescription);
		}else if ("1_15.gif".equals(ratingImageFile)) {
			return new RestaurantRating(4, RatingType.COMFORTABLE, ratingDescription);
		}else if ("1_16.gif".equals(ratingImageFile)) {
			return new RestaurantRating(5, RatingType.COMFORTABLE, ratingDescription);
		}else if ("1_17.gif".equals(ratingImageFile)) {
			return new RestaurantRating(1, RatingType.PLEASANT, ratingDescription);
		}else if ("1_18.gif".equals(ratingImageFile)) {
			return new RestaurantRating(2, RatingType.PLEASANT, ratingDescription);
		}else if ("1_19.gif".equals(ratingImageFile)) {
			return new RestaurantRating(3, RatingType.PLEASANT, ratingDescription);
		}else if ("1_20.gif".equals(ratingImageFile)) {
			return new RestaurantRating(4, RatingType.PLEASANT, ratingDescription);
		}else if ("1_21.gif".equals(ratingImageFile)) {
			return new RestaurantRating(5, RatingType.PLEASANT, ratingDescription);
		}else if ("1_282.gif".equals(ratingImageFile)) {
			return new RestaurantRating(1, RatingType.PUB, ratingDescription);
		}else if ("1_504.gif".equals(ratingImageFile)) {
			return new RestaurantRating(1, RatingType.HOTEL, ratingDescription);
		}else if ("4_1.gif".equals(ratingImageFile)) {
			return new RestaurantRating(1, RatingType.MICHELIN_STAR, ratingDescription);
		}else if ("4_2.gif".equals(ratingImageFile)) {
			return new RestaurantRating(2, RatingType.MICHELIN_STAR, ratingDescription);
		}else if ("4_3.gif".equals(ratingImageFile)) {
			return new RestaurantRating(3, RatingType.MICHELIN_STAR, ratingDescription);
		}
		return null;
	}

	private void recordError(String string) {
		errors.add(string);
	}
	
	public List<String> getErrors() {
		return errors;
	}

	private String getField(JSONObject result, String key) {
		Object value = getJsonValue(result, key);
		try {
			return (String) value;
		}catch(ClassCastException e) {
			recordError("Expected string field for key: " + key + ", but got " + value);
			return null;
		}
	}

	private int getIntField(JSONObject result, String key) {
		Object value = getJsonValue(result, key);
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

	private float getFloatField(JSONObject result, String key) {
		Object value = getJsonValue(result, key);
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

	private String getListItem(JSONArray jsonArray, int index) {
		Object value = getJsonValueFromArray(jsonArray, index);
		try {
			return (String) value;
		}catch(ClassCastException e) {
			recordError("Expected string field at array index " + index + ", but got: " + value);
		}
		return null;
	}

	private JSONArray getListField(JSONObject result, String key) {
		Object value = getJsonValue(result, key);
		try {
			if (value != null) {
				return (JSONArray) value;
			}
		}catch(ClassCastException e) {
			recordError("Expected list field for key: " + key + ", but got: " + value);
		}
		return new JSONArray();
	}

	private JSONObject getMapField(JSONObject result, String key) {
		Object value = getJsonValue(result, key);
		try {
			if (value != null) {
				return(JSONObject) value;
			}
		}catch(ClassCastException e) {
			recordError("Expected map field for key: " + key + ", but got: " + value);
		}
		return new JSONObject();
	}
	
	private Object getJsonValue(JSONObject jsonObject, String key) {
		try {
			return jsonObject.get(key);
		}catch(JSONException e) {
			recordError("JSON field not found. key: " + key + ", object: " + jsonObject);
		}
		return null;
	}
	
	private Object getJsonValueFromArray(JSONArray jsonArray, int index) {
		if (index < jsonArray.length()) {
			try {
				return jsonArray.get(index);
			}catch(JSONException e) {
				//Should never happen as we have already checked the array length
				recordError("JSON field not found in array. Index: " + index + ", array: " + jsonArray);
			}
		}else{
			recordError("Index not found in array. Index: " + index + ", array size: " + jsonArray.length());
		}
		return null;
	}
	
}
