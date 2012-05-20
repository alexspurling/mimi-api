package com.sandstonelabs.mimi;

public class RestaurantRating {

	public final int ratingValue;
	public final RatingType ratingType;
	
	public RestaurantRating(int ratingValue, RatingType ratingType) {
		this.ratingValue = ratingValue;
		this.ratingType = ratingType;
	}

	@Override
	public String toString() {
		return "RestaurantRating [ratingValue=" + ratingValue + ", ratingType=" + ratingType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (ratingType != other.ratingType)
			return false;
		if (ratingValue != other.ratingValue)
			return false;
		return true;
	}
	
}
