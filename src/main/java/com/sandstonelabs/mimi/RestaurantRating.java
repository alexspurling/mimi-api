package com.sandstonelabs.mimi;

public class RestaurantRating {

	public static enum RatingType {
		COMFORTABLE,
		PLEASANT,
		MICHELIN_STAR,
		PUB,
		HOTEL
	}
	
	public final int ratingValue;
	public final RatingType ratingType;
	public final String description;
	
	public RestaurantRating(int ratingValue, RatingType ratingType, String description) {
		this.ratingValue = ratingValue;
		this.ratingType = ratingType;
		this.description = description;
	}

	@Override
	public String toString() {
		return "RestaurantRating [ratingValue=" + ratingValue + ", ratingType=" + ratingType + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((ratingType == null) ? 0 : ratingType.hashCode());
		result = prime * result + ratingValue;
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
		RestaurantRating other = (RestaurantRating) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (ratingType != other.ratingType)
			return false;
		if (ratingValue != other.ratingValue)
			return false;
		return true;
	}
	
}
