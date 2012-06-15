package com.sandstonelabs.mimi;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.junit.Test;

public class CachedRestaurantSearchTest {

	@Test
	public void teststoreResultsInCache_WriteToEmptyCacheFile_newEntriesAreRecorded() throws IOException, JSONException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		File cacheFile = newEmptyCacheFile();
		
		RestaurantJsonCache restaurantJsonCache1 = new RestaurantJsonCache(cacheFile, jsonParser);
		
		float searchLatitude = 51.493f;
		float searchLongitude = -0.168f;
		String restaurantString1 = "{ coords : {lon : -0.16886, lat : 51.49323}, name : \"Bibendum\", vX : \"Michelin House, 81 Fulham Rd. GB - Chelsea SW3 6RD\", address : \"Michelin House, 81 Fulham Rd.\", zipCode : \"SW3 6RD\", city : \"Chelsea\", countryLabel : \"United Kingdom\", sO : \"GBR\", sQ : \"1138\", aac : \"http://www.bibendum.co.uk\", id : \"69949\", index : 7, distance : 135, CZ : \"19\", iconIdWithoutExt : \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/map_icon/mapiconpoi19\", URL : \"\", NM : [\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"], vE : [     \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"Menu: 30£ (lunch) - Carte: 36£/63£ <img src='http://www.viamichelin.com/viamichelin/gbr/img/rg/513.gif' alt='A particularly  interesting wine list'>\"   ,  \"\"   ,  \"French classic\"   ,  \"Has maintained a loyal following for over 20 years, with its French food that comes with a British accent. Located on the 1st floor of a London landmark - Michelin's former HQ, dating from 1911.\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"    ], Jk : [     \"\"   ,  \"\"   ,  \"\"   ,  \"GBP\"    ], Jj : [  \"1\"  ], CY : [ \"\",\"\" , \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/images/1_19.gif\", \"Very comfortable and pleasant restaurant\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\"  ], ro : \"TEMPLATE_PRODUCTS/41102\", productId : 41102, YV : \"RestaurantSearch\", Xl : \"GR\", Pp : \"GR_Restaurant\", criteriaList : [], ED : true, principalNature : \"restaurant\", principalType : \"restaurant\", secondType : \"redGuide\", isMagazine : false, isClassified : false,  pz:5.0, PS:2, commentCount:2, isHotel : false, Ik : \"reservations@bibendum.co.uk\", telephone : \"(020) 75815817\", zs : \"(020) 78237925\", reflexId:\"185790\",  etgvDestination : \"Great_Britain-London\", encodedName : \"Bibendum\" }"; 
		String restaurantString2 = "{ coords : {lon : -0.16861, lat : 51.49369}, name : \"Joe's\", vX : \"126 Draycott Ave GB - Chelsea SW3 3AH\", address : \"126 Draycott Ave\", zipCode : \"SW3 3AH\", city : \"Chelsea\", countryLabel : \"United Kingdom\", sO : \"GBR\", sQ : \"1138\", aac : \"http://www.joseph.co.uk\", id : \"332878\", index : 8, distance : 141, CZ : \"13\", iconIdWithoutExt : \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/map_icon/mapiconpoi13\", URL : \"\", NM : [\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"], vE : [     \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"Menu: 17£ (lunch) - Carte: 24£/37£\"   ,  \"\"   ,  \"modern\"   ,  \"Back in the '80s when the only thing bigger than the hair were the shoulder pads, Joe's was the place to be seen. It's now fashionable once again and its appealing, fortnightly changing menu comes with Mediterranean overtones.\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"    ], Jk : [     \"\"   ,  \"\"   ,  \"\"   ,  \"GBP\"    ], Jj : [  \"1\"  ], CY : [ \"\",\"\" , \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/images/1_13.gif\", \"Comfortable restaurant\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\"  ], ro : \"TEMPLATE_PRODUCTS/41102\", productId : 41102, YV : \"RestaurantSearch\", Xl : \"GR\", Pp : \"GR_Restaurant\", criteriaList : [], ED : true, principalNature : \"restaurant\", principalType : \"restaurant\", secondType : \"redGuide\", isMagazine : false, isClassified : false,  pz:0.0, PS:2, commentCount:0, isHotel : false, Ik : \"reservations@joseph.co.uk\", telephone : \"(020) 72252217\", zs : \"\", reflexId:\"185790\",  etgvDestination : \"Great_Britain-London\", encodedName : \"Joe_s\" }";
		List<String> jsonToBeStored = Arrays.asList(restaurantString1, restaurantString2);
		
		restaurantJsonCache1.storeResultsInCache(jsonToBeStored, searchLatitude, searchLongitude);
		
		RestaurantJsonCache restaurantJsonCache2 = new RestaurantJsonCache(cacheFile, jsonParser);
		
		Set<Restaurant> allCachedRestaurants = restaurantJsonCache2.getAllCachedRestaurants();
		
		Restaurant restaurant1 = jsonParser.parseRestaurantSearchResultsFromJson(restaurantString1);
		Restaurant restaurant2 = jsonParser.parseRestaurantSearchResultsFromJson(restaurantString2);
		List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
		
		//Can't compare the two lists directly as the cache can return
		//them in any order. We do contains on both lists to make sure they
		//are the same - TODO use Set?
		assertTrue(expectedRestaurants.containsAll(allCachedRestaurants));
		assertTrue(allCachedRestaurants.containsAll(expectedRestaurants));
	}

	private File newEmptyCacheFile() throws IOException {
		File cacheFile = new File("test-cache.txt");
		cacheFile.delete(); //Make sure the file does not exist
		return cacheFile;
	}
	
}

