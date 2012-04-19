package com.sandstonelabs.mimi;

public class RestaurantDetails {

	//Could be useful to parse the facilities (such as air-conditioning, non-smoking etc)
	public final String openingTimes;
	public final String thingsToKnow;

	private RestaurantDetails(RestaurantDetailsBuilder builder) {
		openingTimes = builder.openingTimes;
		thingsToKnow = builder.thingsToKnow;
	}
	
	public static class RestaurantDetailsBuilder {
		private String openingTimes;
		private String thingsToKnow;
		
		public void openingTimes(String openingTimes) {
			this.openingTimes = openingTimes;
		}
		
		public void thingsToKnow(String thingsToKnow) {
			this.thingsToKnow = thingsToKnow;
		}
		
		public RestaurantDetails build() {
			return new RestaurantDetails(this);
		}
		
	}
	
}
