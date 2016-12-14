/**
 * 
 */
package com.docume.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.docume.pojo.ModelDetail;
import com.docume.pojo.MyModel;

import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class ResponseModelDataTest {

	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource("swg.yml").getFile());
	Swagger swagger = null;
	
	@Before
	public void createSwagger() {
		if (file.exists()) {
			swagger = new SwaggerParser().read("swg.yml");
		}

	}

	/**
	 * Test method for {@link com.amadeus.docume.util.ResponseModelData#createModelList(io.swagger.models.Swagger)}.
	 */
	@Test
	public void testCreateModelList() {
		ResponseModelData responseModelData = new ResponseModelData();
		List<ModelDetail> modelList = responseModelData.createModelList(swagger);
		Set<String> expectedTitles = swagger.getDefinitions().keySet();
		
		for(ModelDetail md : modelList){
			String actualTitle = md.getModelTitle();
			assertTrue(expectedTitles.contains(actualTitle));
			
			Model expectedModel = swagger.getDefinitions().get(actualTitle);
		}
		ModelDetail modelDetail = modelList.get(2);
		MyModel model = modelDetail.getModelList().get(0);
		assertEquals("destination", model.getModelName());
		assertEquals("The <a href=\"https://en.wikipedia.org/wiki/International_Air_Transport_Association_airport_code\">IATA code</a> of the city or airport to which the traveler may go, from the provided origin", model.getModelDesc());
		assertEquals("string", model.getModelType());
		/*expectedList.add("Error");
		expectedList.add("ExtremeSearchResponse");
		expectedList.add("ExtremeSearchResult");
		expectedList.add("LowFareSearchResponse");
		expectedList.add("LowFareSearchResult");
		expectedList.add("FlightSearchItinerary");
		expectedList.add("FlightSearchBound");
		expectedList.add("FlightSearchSegment");
		expectedList.add("Airport");
		expectedList.add("FlightSearchBookingInfo");
		expectedList.add("FlightSearchPrice");
		expectedList.add("FareRestrictions");
		expectedList.add("Fare");
		expectedList.add("AirportAutocompleteResponse");
		expectedList.add("RailStationAutocompleteResponse");
		expectedList.add("LocationResponse");
		expectedList.add("CityInformation");
		expectedList.add("AirportInformation");
		expectedList.add("NearestAirport");
		expectedList.add("Geolocation");
		expectedList.add("HotelSearchResponse");
		expectedList.add("HotelPropertyResponse");
		expectedList.add("Address");
		expectedList.add("Contact");
		expectedList.add("Amenity");
		expectedList.add("Award");
		expectedList.add("Image");
		expectedList.add("HotelRoom");
		expectedList.add("RoomInfo");
		expectedList.add("Amount");
		expectedList.add("PeriodRate");
		expectedList.add("Link");
		expectedList.add("CarSearchResponse");
		expectedList.add("CarSearchResult");
		expectedList.add("Company");
		expectedList.add("Vehicle");
		expectedList.add("VehicleInfo");
		expectedList.add("Rate");
		expectedList.add("ExtensiveTrainSearchResponse");
		expectedList.add("ExtensiveTrainSearchResult");
		expectedList.add("TrainSearchItinerary");
		expectedList.add("Station");
		expectedList.add("TrainSearchSegment");
		expectedList.add("TrainSearchPricing");
		expectedList.add("RestrictedRate");
		expectedList.add("RailStationResponse");
		expectedList.add("TrainScheduleSearchResponse");
		expectedList.add("TrainScheduleSearchResult");
		expectedList.add("RailService");
		expectedList.add("TravelRecordResponse");
		expectedList.add("TravelRecordHeader");
		expectedList.add("Message");
		expectedList.add("Traveler");
		expectedList.add("Infant");
		expectedList.add("FrequentTravelerCard");
		expectedList.add("Reservation");
		expectedList.add("FlightTicket");
		expectedList.add("FlightReservationBound");
		expectedList.add("FlightReservationSegment");
		expectedList.add("FlightReservationBookingInfo");
		expectedList.add("CarReservation");
		expectedList.add("CarReservationBookingInfo");
		expectedList.add("HotelReservation");
		expectedList.add("HotelReservationBookingInfo");
		expectedList.add("OtherReservation");
		expectedList.add("OtherReservationBookingInfo");
		expectedList.add("AffiliateSearchResponse");
		expectedList.add("AffiliateSearchMeta");
		expectedList.add("CarrierMeta");
		expectedList.add("CarrierInfo");
		expectedList.add("Logos");
		expectedList.add("AffiliateSearchResult");
		expectedList.add("AffiliatePayout");
		expectedList.add("AffiliateFlightSearchPrice");
		expectedList.add("Fees");
		expectedList.add("TopDestinationsSearchResponse");
		expectedList.add("TopDestinationsSearchResult");
		expectedList.add("TopSearchesSearchResponse");
		expectedList.add("TopSearchesSearchResult");
		expectedList.add("FlightTrafficSearchResponse");
		expectedList.add("FlightTrafficSearchResult");
		expectedList.add("PointsOfInterestResponse");
		expectedList.add("PointOfInterestCity");
		expectedList.add("PointOfInterestResult");
		expectedList.add("PointOfInterestDetails");
		expectedList.add("ImageSize");*/

//		assertEquals(expectedList, actualList);
		
	}


}
