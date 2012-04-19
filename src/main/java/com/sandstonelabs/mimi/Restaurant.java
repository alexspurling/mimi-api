package com.sandstonelabs.mimi;

public class Restaurant {
	
	public final int id;
	public final int productId;
	public final float latitude;
	public final float longitude;
	
	public final String name;
	public final String description;
	public final String cuisine;
	public final String foodPrice;
	
	public final String email;
	public final String phoneNumber;
	public final String oneLineAddress;
	public final String address;
	public final String city;
	public final String zipCode;
	public final String countryCode;
	public final String country;
	public final String website;
	
	private Restaurant(RestaurantBuilder builder) {
		id = builder.id;
		productId = builder.productId;
		latitude = builder.latitude;
		longitude = builder.longitude;

		name = builder.name;
		description = builder.description;
		cuisine = builder.cuisine;
		foodPrice = builder.foodPrice;

		email = builder.email;
		phoneNumber = builder.phoneNumber;
		oneLineAddress = builder.oneLineAddress;
		address = builder.address;
		city = builder.city;
		zipCode = builder.zipCode;
		countryCode = builder.countryCode;
		country = builder.country;
		website = builder.website;
	}
	
	public static class RestaurantBuilder {

		private int id;
		private int productId;
		private float latitude;
		private float longitude;
		
		private String name;
		private String description;
		private String cuisine;
		private String foodPrice;
		
		private String email;
		private String phoneNumber;
		private String oneLineAddress;
		private String address;
		private String city;
		private String zipCode;
		private String countryCode;
		private String country;
		private String website;
		
		public void id(int id) {
			this.id = id;
		}

		public void productId(int productId) {
			this.productId = productId;
		}

		public void latitude(float latitude) {
			this.latitude = latitude;
		}

		public void longitude(float longitude) {
			this.longitude = longitude;
		}

		public void name(String name) {
			this.name = name;
		}

		public void description(String description) {
			this.description = description;
		}

		public void cuisine(String cuisine) {
			this.cuisine = cuisine;
		}

		public void foodPrice(String foodPrice) {
			this.foodPrice = foodPrice;
		}

		public void email(String email) {
			this.email = email;
		}

		public void phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public void oneLineAddress(String oneLineAddress) {
			this.oneLineAddress = oneLineAddress;
		}

		public void address(String address) {
			this.address = address;
		}

		public void city(String city) {
			this.city = city;
		}

		public void zipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public void countryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public void country(String country) {
			this.country = country;
		}

		public void website(String website) {
			this.website = website;
		}

		public Restaurant build() {
			return new Restaurant(this);
		}
	}
	
	private RestaurantDetails restaurantDetails;

	public void setRestaurantDetails(RestaurantDetails restaurantDetails) {
		this.restaurantDetails = restaurantDetails;
	}

	public RestaurantDetails getRestaurantDetails() {
		return restaurantDetails;
	}
	
	

}
