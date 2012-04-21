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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((cuisine == null) ? 0 : cuisine.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((foodPrice == null) ? 0 : foodPrice.hashCode());
		result = prime * result + id;
		result = prime * result + Float.floatToIntBits(latitude);
		result = prime * result + Float.floatToIntBits(longitude);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((oneLineAddress == null) ? 0 : oneLineAddress.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + productId;
		result = prime * result + ((website == null) ? 0 : website.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurant other = (Restaurant) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (cuisine == null) {
			if (other.cuisine != null)
				return false;
		} else if (!cuisine.equals(other.cuisine))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (foodPrice == null) {
			if (other.foodPrice != null)
				return false;
		} else if (!foodPrice.equals(other.foodPrice))
			return false;
		if (id != other.id)
			return false;
		if (Float.floatToIntBits(latitude) != Float.floatToIntBits(other.latitude))
			return false;
		if (Float.floatToIntBits(longitude) != Float.floatToIntBits(other.longitude))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (oneLineAddress == null) {
			if (other.oneLineAddress != null)
				return false;
		} else if (!oneLineAddress.equals(other.oneLineAddress))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (productId != other.productId)
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

}
