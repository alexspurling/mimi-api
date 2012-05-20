package com.sandstonelabs.mimi;

import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.junit.Test;

import com.sandstonelabs.mimi.RestaurantRating.RatingType;

public class RestaurantJsonConverterTest {

	@Test
	public void testLoadRestaurantFromJson() throws JSONException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		String jsonData = "{ coords : {lon : -0.1633, lat : 51.48897}, name : \"Phoenix\", vX : \"23 Smith St GB - Chelsea SW3 4EE\", address : \"23 Smith St\", zipCode : \"SW3 4EE\", city : \"Chelsea\", countryLabel : \"United Kingdom\", sO : \"GBR\", sQ : \"1138\", aac : \"http://www.geronimo-inns.co.uk/thepheonix\", id : \"120154\", index : 19, distance : 509, CZ : \"282\", iconIdWithoutExt : \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/map_icon/mapiconpoi282\", URL : \"\", NM : [\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"], vE : [     \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"Carte: 21£/34£\"   ,  \"\"   ,  \"modern\"   ,  \"Friendly, conscientiously run Chelsea local, where satisfying and carefully prepared pub classics are served in the roomy, civilised bar or in the warm, comfortable dining room at the back.\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"    ], Jk : [     \"\"   ,  \"\"   ,  \"\"   ,  \"GBP\"    ], Jj : [  \"1\"  ], CY : [ \"\",\"\" , \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/images/1_282.gif\", \"Traditional pubs serving food\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\"  ], ro : \"TEMPLATE_PRODUCTS/41102\", productId : 41102, YV : \"RestaurantSearch\", Xl : \"GR\", Pp : \"GR_Restaurant\", criteriaList : [], ED : true, principalNature : \"restaurant\", principalType : \"restaurant\", secondType : \"redGuide\", isMagazine : false, isClassified : false,  pz:0.0, PS:2, commentCount:0, isHotel : false, Ik : \"thephoenix@geronimo-inns.co.uk\", telephone : \"(020) 77309182\", zs : \"\", reflexId:\"185790\",  etgvDestination : \"Great_Britain-London\", encodedName : \"Phoenix\" }";
		Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
		
		Restaurant phoenix = new Restaurant.RestaurantBuilder().
			id(120154).
			productId(41102).
			latitude(51.48897f).
			longitude(-0.1633f).
			name("Phoenix").
			description("Friendly, conscientiously run Chelsea local, where satisfying and carefully prepared pub classics are served in the roomy, civilised bar or in the warm, comfortable dining room at the back.").
			cuisine("modern").
			foodPrice("Carte: 21£/34£").
			rating(new RestaurantRating(1, RatingType.PUB)).
			email("thephoenix@geronimo-inns.co.uk").
			phoneNumber("(020) 77309182").
			oneLineAddress("23 Smith St GB - Chelsea SW3 4EE").
			address("23 Smith St").
			city("Chelsea").
			zipCode("SW3 4EE").
			countryCode("GBR").
			country("United Kingdom").
			website("http://www.geronimo-inns.co.uk/thepheonix").build();
		
		assertEquals("There were json parsing errors: " + jsonParser.getErrors(), 0, jsonParser.getErrors().size());
		assertEquals(phoenix, restaurant);
	}

}
