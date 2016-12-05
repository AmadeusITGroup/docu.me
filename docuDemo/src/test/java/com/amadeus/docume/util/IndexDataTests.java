/**
 * 
 */
package com.amadeus.docume.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.amadeus.docume.util.IndexData;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class IndexDataTests {

	String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";
	File file = new File(swaggerFile);

	/**
	 * Test method for {@link com.amadeus.docuMe.util.IndexData#createIndexData(io.swagger.models.Swagger)}.
	 */
	@Test
	public void testCreateIndexData() {
		Swagger swagger = null;
		if (file.exists()) {
			swagger = new SwaggerParser().read(swaggerFile);
		}
		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();
		Map<String, Path> pathMap = swagger.getPaths();
		Map<String, Model> modelMap = swagger.getDefinitions();

		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			Path path = pathDetail.getValue();
			List<Operation> opList = path.getOperations();
			for (Operation op : opList) {
				operationIdList.add(op.getOperationId());
			}
		}
		List<String> expectedOperationIdList = new ArrayList<>();
		expectedOperationIdList.add("Airport Autocomplete");
		expectedOperationIdList.add("Nearest Relevant Airport");
		expectedOperationIdList.add("Car Rental Airport Search");
		expectedOperationIdList.add("Car Rental Geosearch");
		expectedOperationIdList.add("Flight Affiliate Search");
		expectedOperationIdList.add("Flight Extensive Search");
		expectedOperationIdList.add("Flight Inspiration Search");
		expectedOperationIdList.add("Flight Low-Fare Search");
		expectedOperationIdList.add("Hotel Airport Search");
		expectedOperationIdList.add("Hotel Geosearch by box");
		expectedOperationIdList.add("Hotel Geosearch by circle");
		expectedOperationIdList.add("Hotel Property Code Search");
		expectedOperationIdList.add("Location Information");
		expectedOperationIdList.add("YapQ Geosearch");
		expectedOperationIdList.add("YapQ City Name Search");
		expectedOperationIdList.add("Rail-Station Information");
		expectedOperationIdList.add("Rail-Station Autocomplete");
		expectedOperationIdList.add("Train Extensive Search");
		expectedOperationIdList.add("Train Schedule Search");
		expectedOperationIdList.add("Flight Traffic API");
		expectedOperationIdList.add("Top Flight Destinations");
		expectedOperationIdList.add("Top Flight Searches");
		expectedOperationIdList.add("Travel Record Retrieve");
		
		assertArrayEquals(expectedOperationIdList.toArray(), operationIdList.toArray());
		
		List<String> expectedModelList = new ArrayList<>();
		expectedModelList.add("Error");
		expectedModelList.add("ExtremeSearchResponse");
		expectedModelList.add("ExtremeSearchResult");
		expectedModelList.add("LowFareSearchResponse");
		expectedModelList.add("LowFareSearchResult");
		expectedModelList.add("FlightSearchItinerary");
		expectedModelList.add("FlightSearchBound");
		expectedModelList.add("FlightSearchSegment");
		expectedModelList.add("Airport");
		expectedModelList.add("FlightSearchBookingInfo");
		expectedModelList.add("FlightSearchPrice");
		expectedModelList.add("FareRestrictions");
		expectedModelList.add("Fare");
		expectedModelList.add("AirportAutocompleteResponse");
		expectedModelList.add("RailStationAutocompleteResponse");
		expectedModelList.add("LocationResponse");
		expectedModelList.add("CityInformation");
		expectedModelList.add("AirportInformation");
		expectedModelList.add("NearestAirport");
		expectedModelList.add("Geolocation");
		expectedModelList.add("HotelSearchResponse");
		expectedModelList.add("HotelPropertyResponse");
		expectedModelList.add("Address");
		expectedModelList.add("Contact");
		expectedModelList.add("Amenity");
		expectedModelList.add("Award");
		expectedModelList.add("Image");
		expectedModelList.add("HotelRoom");
		expectedModelList.add("RoomInfo");
		expectedModelList.add("Amount");
		expectedModelList.add("PeriodRate");
		expectedModelList.add("Link");
		expectedModelList.add("CarSearchResponse");
		expectedModelList.add("CarSearchResult");
		expectedModelList.add("Company");
		expectedModelList.add("Vehicle");
		expectedModelList.add("VehicleInfo");
		expectedModelList.add("Rate");
		expectedModelList.add("ExtensiveTrainSearchResponse");
		expectedModelList.add("ExtensiveTrainSearchResult");
		expectedModelList.add("TrainSearchItinerary");
		expectedModelList.add("Station");
		expectedModelList.add("TrainSearchSegment");
		expectedModelList.add("TrainSearchPricing");
		expectedModelList.add("RestrictedRate");
		expectedModelList.add("RailStationResponse");
		expectedModelList.add("TrainScheduleSearchResponse");
		expectedModelList.add("TrainScheduleSearchResult");
		expectedModelList.add("RailService");
		expectedModelList.add("TravelRecordResponse");
		expectedModelList.add("TravelRecordHeader");
		expectedModelList.add("Message");
		expectedModelList.add("Traveler");
		expectedModelList.add("Infant");
		expectedModelList.add("FrequentTravelerCard");
		expectedModelList.add("Reservation");
		expectedModelList.add("FlightTicket");
		expectedModelList.add("FlightReservationBound");
		expectedModelList.add("FlightReservationSegment");
		expectedModelList.add("FlightReservationBookingInfo");
		expectedModelList.add("CarReservation");
		expectedModelList.add("CarReservationBookingInfo");
		expectedModelList.add("HotelReservation");
		expectedModelList.add("HotelReservationBookingInfo");
		expectedModelList.add("OtherReservation");
		expectedModelList.add("OtherReservationBookingInfo");
		expectedModelList.add("AffiliateSearchResponse");
		expectedModelList.add("AffiliateSearchMeta");
		expectedModelList.add("CarrierMeta");
		expectedModelList.add("CarrierInfo");
		expectedModelList.add("Logos");
		expectedModelList.add("AffiliateSearchResult");
		expectedModelList.add("AffiliatePayout");
		expectedModelList.add("AffiliateFlightSearchPrice");
		expectedModelList.add("Fees");
		expectedModelList.add("TopDestinationsSearchResponse");
		expectedModelList.add("TopDestinationsSearchResult");
		expectedModelList.add("TopSearchesSearchResponse");
		expectedModelList.add("TopSearchesSearchResult");
		expectedModelList.add("FlightTrafficSearchResponse");
		expectedModelList.add("FlightTrafficSearchResult");
		expectedModelList.add("PointsOfInterestResponse");
		expectedModelList.add("PointOfInterestCity");
		expectedModelList.add("PointOfInterestResult");
		expectedModelList.add("PointOfInterestDetails");
		expectedModelList.add("ImageSize");
		
		if (modelMap != null) {
			// Add model's list in index file
			for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
				modelsList.add(entry.getKey());
			}
		}
		
		assertEquals(expectedModelList, modelsList);
	}

}
