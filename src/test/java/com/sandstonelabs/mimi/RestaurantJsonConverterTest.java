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
			cuisine("Modern").
			foodPrice("Carte: 21£/34£").
			comfortRating(new RestaurantRating(1, RatingType.PUB, "Traditional pubs serving food")).
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
	
	@Test
	public void testGordonRamsey() throws JSONException {
		RestaurantJsonParser jsonParser = new RestaurantJsonParser();
		
		String jsonData = "{ coords : {lon : -0.16201, lat : 51.4855}, name : \"Gordon Ramsay\", vX : \"68-69 Royal Hospital Rd. GB - Chelsea SW3 4HP\", address : \"68-69 Royal Hospital Rd.\", zipCode : \"SW3 4HP\", city : \"Chelsea\", countryLabel : \"United Kingdom\", sO : \"GBR\", sQ : \"1138\", aac : \"http://www.gordonramsay.com\", id : \"69945\", index : 8, distance : 512, CZ : \"15\", iconIdWithoutExt : \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/map_icon/mapiconpoi15\", URL : \"\", NM : [\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"], vE : [     \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"Menu: 45£/ 90£ <img src='http://www.viamichelin.com/viamichelin/gbr/img/rg/513.gif' alt='A particularly  interesting wine list'>\"   ,  \"\"   ,  \"French classic\"   ,  \"Head Chef Clare Smyth has brought a lighter, more instinctive style to the cooking while still delivering on the Gordon Ramsay classics. The elegant simplicity of the room exudes calmness; service is equally composed and well-organised.\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"   ,  \"\"    ], Jk : [     \"\"   ,  \"\"   ,  \"\"   ,  \"GBP\"    ], Jj : [  \"1\"  ], CY : [ \"\",\"\" , \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/images/1_15.gif\", \"Top class comfort restaurant\" , \"http://www.viamichelin.com/viamichelin/client_data/TEMPLATE_PRODUCTS/41102/gbr/images/4_3.gif\", \"Exceptional cuisine, worth a special journey\" , \"\", \"\" , \"\", \"\" , \"\", \"\" , \"\", \"\"  ], ro : \"TEMPLATE_PRODUCTS/41102\", productId : 41102, YV : \"RestaurantSearch\", Xl : \"GR\", Pp : \"GR_Restaurant\", criteriaList : [], ED : true, principalNature : \"restaurant\", principalType : \"restaurant\", secondType : \"redGuide\", isMagazine : false, isClassified : false,  pz:4.882352941176471, PS:4, commentCount:17, isHotel : false, Ik : \"royalhospitalroad@gordonramsay.com\", telephone : \"(020) 73524441\", zs : \"(020) 75921213\", reflexId:\"185790\",  etgvDestination : \"Great_Britain-London\", encodedName : \"Gordon_Ramsay\" }";
		Restaurant restaurant = jsonParser.parseRestaurantSearchResultsFromJson(jsonData);
		
		Restaurant phoenix = new Restaurant.RestaurantBuilder().
			id(69945).
			productId(41102).
			latitude(51.4855f).
			longitude(-0.16201f).
			name("Gordon Ramsay").
			description("Head Chef Clare Smyth has brought a lighter, more instinctive style to the cooking while still delivering on the Gordon Ramsay classics. The elegant simplicity of the room exudes calmness; service is equally composed and well-organised.").
			cuisine("French classic").
			foodPrice("Menu: 45£/ 90£ <img src='http://www.viamichelin.com/viamichelin/gbr/img/rg/513.gif' alt='A particularly  interesting wine list'>").
			comfortRating(new RestaurantRating(4, RatingType.COMFORTABLE, "Top class comfort restaurant")).
			qualityRating(new RestaurantRating(3, RatingType.MICHELIN_STAR, "Exceptional cuisine, worth a special journey")).
			email("royalhospitalroad@gordonramsay.com").
			phoneNumber("(020) 73524441").
			oneLineAddress("68-69 Royal Hospital Rd. GB - Chelsea SW3 4HP").
			address("68-69 Royal Hospital Rd.").
			city("Chelsea").
			zipCode("SW3 4HP").
			countryCode("GBR").
			country("United Kingdom").
			website("http://www.gordonramsay.com").build();
		
		assertEquals("There were json parsing errors: " + jsonParser.getErrors(), 0, jsonParser.getErrors().size());
		assertEquals(phoenix, restaurant);
	}

}
