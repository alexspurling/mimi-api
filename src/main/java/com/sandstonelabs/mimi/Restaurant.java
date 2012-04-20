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
		
		public RestaurantBuilder id(int id) {
			this.id = id; 
			return this;
		}

		public RestaurantBuilder productId(int productId) {
			this.productId = productId; 
			return this;
		}

		public RestaurantBuilder latitude(float latitude) {
			this.latitude = latitude; 
			return this;
		}

		public RestaurantBuilder longitude(float longitude) {
			this.longitude = longitude; 
			return this;
		}

		public RestaurantBuilder name(String name) {
			this.name = name; 
			return this;
		}

		public RestaurantBuilder description(String description) {
			this.description = description; 
			return this;
		}

		public RestaurantBuilder cuisine(String cuisine) {
			this.cuisine = cuisine; 
			return this;
		}

		public RestaurantBuilder foodPrice(String foodPrice) {
			this.foodPrice = foodPrice; 
			return this;
		}

		public RestaurantBuilder email(String email) {
			this.email = email; 
			return this;
		}

		public RestaurantBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber; 
			return this;
		}

		public RestaurantBuilder oneLineAddress(String oneLineAddress) {
			this.oneLineAddress = oneLineAddress; 
			return this;
		}

		public RestaurantBuilder address(String address) {
			this.address = address; 
			return this;
		}

		public RestaurantBuilder city(String city) {
			this.city = city; 
			return this;
		}

		public RestaurantBuilder zipCode(String zipCode) {
			this.zipCode = zipCode; 
			return this;
		}

		public RestaurantBuilder countryCode(String countryCode) {
			this.countryCode = countryCode; 
			return this;
		}

		public RestaurantBuilder country(String country) {
			this.country = country; 
			return this;
		}

		public RestaurantBuilder website(String website) {
			this.website = website; 
			return this;
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

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", productId=" + productId
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", name=" + name + ", description=" + description
				+ ", cuisine=" + cuisine + ", foodPrice=" + foodPrice
				+ ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", oneLineAddress=" + oneLineAddress + ", address=" + address
				+ ", city=" + city + ", zipCode=" + zipCode + ", countryCode="
				+ countryCode + ", country=" + country + ", website=" + website
				+ ", restaurantDetails=" + restaurantDetails + "]";
	}

}
