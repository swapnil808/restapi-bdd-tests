package stepDefs;

import static io.restassured.RestAssured.given;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;
import Utils.WriteJsonFile;
import Utils.ReadJson;

public class BookingSteps {
	
    private Response response;
    private String bookingRequest;
    private String updateBookingRequest;
    JSONObject bookingDates;
    private JsonPath jp = null;
    public String bookingid;
    private String tokenCode;
    
    String dataFilePath = System.getProperty("user.dir") +  "\\src\\test\\resources\\Data\\data.json";
    
    @Given("^I have details (.*), (.*) and (.*) to make a booking$")
    public void i_have_details_to_make_a_booking(String firstName, String lastName, String additionalNeeds) {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		
		JSONObject bookingRequestObj = new JSONObject();
		bookingRequestObj.put("firstname", firstName);
		bookingRequestObj.put("lastname",lastName);
		bookingRequestObj.put("totalprice","155");
		bookingRequestObj.put( "depositpaid","true");
		bookingRequestObj.put("additionalneeds", additionalNeeds);
		
		bookingDates = new JSONObject();
		bookingDates.put("checkin", "2021-03-15");
		bookingDates.put("checkout", "2021-04-01");
		
		bookingRequestObj.put("bookingdates", bookingDates);
		
		bookingRequest = bookingRequestObj.toString();
		
		WriteJsonFile.writeToJsonFile(dataFilePath,bookingRequestObj);
    }
	

	
	@When("I send Booking request")
	public void i_send_booking_request() {
		response = given()
		.header("Content-Type", "application/json")
		.contentType(ContentType.JSON)
		.body(bookingRequest)
		.when()
		.post("/booking")
		.then()
		.extract()
		.response();
	}

	@Then("I should see my booking got confirmed")
	public void i_should_see_my_booking_got_confirmed() {

		System.out.println("Response : "+response.asString());
		int status = response.getStatusCode();
		Assert.assertEquals(200, status);
		Assert.assertNotNull("Booking Id not found.", response.jsonPath().get("bookingid"));
		WriteJsonFile.updateJson(dataFilePath, "bookingid", response.jsonPath().get("bookingid").toString());
	}

	@Then("I verify that my booking details are correct")
	public void i_verify_that_my_booking_details_are_correct() throws Throwable {

		String firstNameExpected = ReadJson.readJson(dataFilePath, "firstname");
		String lastnameExpected = ReadJson.readJson(dataFilePath, "lastname");
		String totalpriceExpected = ReadJson.readJson(dataFilePath, "totalprice");
		String depositpaidExpected = ReadJson.readJson(dataFilePath, "depositpaid");
		String additionalneedsExpected = ReadJson.readJson(dataFilePath, "additionalneeds");
		String bookingdates = ReadJson.readJson(dataFilePath, "bookingdates");

		
		JSONParser parser = new JSONParser();  
		JSONObject bookingDatesObj = (JSONObject) parser.parse(bookingdates);
		String checkinExpected = bookingDatesObj.get("checkin").toString();
		String checkoutExpected = bookingDatesObj.get("checkout").toString();
		
		System.out.println("Response : "+response.asString());
		
		Assert.assertEquals("firstname value is not equal", firstNameExpected, response.jsonPath().get("booking.firstname"));
		Assert.assertEquals("lastname value is not equal", lastnameExpected, response.jsonPath().get("booking.lastname").toString());
		Assert.assertEquals("totalprice value is not equal", totalpriceExpected, response.jsonPath().get("booking.totalprice").toString());
		Assert.assertEquals("depositpaid value is not equal", depositpaidExpected, response.jsonPath().get("booking.depositpaid").toString());
		Assert.assertEquals("additionalneeds value is not equal", additionalneedsExpected, response.jsonPath().get("booking.additionalneeds").toString());
		Assert.assertEquals("checkin value is not equal", checkinExpected, response.jsonPath().get("booking.bookingdates.checkin").toString());
		Assert.assertEquals("checkout value is not equal", checkoutExpected, response.jsonPath().get("booking.bookingdates.checkout").toString());
		
	}
	
	@Then("I verify that details matches with booking details")
	public void i_verify_that_details_matches_with_booking_details() throws Throwable {

		String firstNameExpected = ReadJson.readJson(dataFilePath, "firstname");
		String lastnameExpected = ReadJson.readJson(dataFilePath, "lastname");
		String totalpriceExpected = ReadJson.readJson(dataFilePath, "totalprice");
		String depositpaidExpected = ReadJson.readJson(dataFilePath, "depositpaid");
		String additionalneedsExpected = ReadJson.readJson(dataFilePath, "additionalneeds");
		String bookingdates = ReadJson.readJson(dataFilePath, "bookingdates");

		
		JSONParser parser = new JSONParser();  
		JSONObject bookingDatesObj = (JSONObject) parser.parse(bookingdates);
		String checkinExpected = bookingDatesObj.get("checkin").toString();
		String checkoutExpected = bookingDatesObj.get("checkout").toString();
		
		System.out.println("Response : "+response.asString());
		
		Assert.assertEquals("firstname value is not equal", firstNameExpected, response.jsonPath().get("firstname"));
		Assert.assertEquals("lastname value is not equal", lastnameExpected, response.jsonPath().get("lastname"));
		Assert.assertEquals("totalprice value is not equal", totalpriceExpected, response.jsonPath().get("totalprice").toString());
		Assert.assertEquals("depositpaid value is not equal", depositpaidExpected, response.jsonPath().get("depositpaid").toString());
		Assert.assertEquals("additionalneeds value is not equal", additionalneedsExpected, response.jsonPath().get("additionalneeds"));
		Assert.assertEquals("checkin value is not equal", checkinExpected, response.jsonPath().get("bookingdates.checkin").toString());
		Assert.assertEquals("checkout value is not equal", checkoutExpected, response.jsonPath().get("bookingdates.checkout").toString());
		
	}

	@Given("I am a Authenticated User")
	public void i_am_a_authenticated_user() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		
		JSONObject authRequestObj = new JSONObject();
		authRequestObj.put("username", "admin");
		authRequestObj.put("password","password123");
		
		tokenCode = given()
		.header("Content-Type", "application/json")
		.contentType(ContentType.JSON)
		.body(authRequestObj.toString())
		.when()
		.post("/auth")
		.then()
		.extract()
		.response()
		.jsonPath()
		.get("token");
		
	}


	
	@When("^I send request to update booking details with (.*), (.*) and (.*)$")
	public void i_send_request_to_update_booking_details_with(String firstName, String lastName, String additionalNeeds) {

		JSONObject updatebookingRequestObj = new JSONObject();
		updatebookingRequestObj.put("firstname", firstName);
		updatebookingRequestObj.put("lastname", lastName);
		updatebookingRequestObj.put("totalprice","175");
		updatebookingRequestObj.put( "depositpaid","true");
		updatebookingRequestObj.put("additionalneeds", additionalNeeds);
		
		bookingDates = new JSONObject();
		bookingDates.put("checkin", "2021-02-15");
		bookingDates.put("checkout", "2021-03-01");
		
		updatebookingRequestObj.put("bookingdates", bookingDates);
		
		updateBookingRequest = updatebookingRequestObj.toString();
		
		WriteJsonFile.writeToJsonFile(dataFilePath,updatebookingRequestObj);
		WriteJsonFile.updateJson(dataFilePath, "bookingid", bookingid);
		
		String token = "token="+tokenCode;
		response = given()
		.header("Content-Type", "application/json")
		.header("Cookie",token)
		.contentType(ContentType.JSON)
		.pathParam("bookingId", bookingid)
		.body(updateBookingRequest)
		.when()
		.put("/booking/{bookingId}")
		.then()
		.extract()
		.response();
	}


	@Then("I should see my booking details got updated")
	public void i_should_see_my_booking_details_got_updated() {
		int status = response.getStatusCode();
		Assert.assertEquals(200, status);
	}

	@Then("I verify that my updated booking details are correct")
	public void i_verify_that_my_updated_booking_details_are_correct() throws Throwable {
		i_verify_that_details_matches_with_booking_details();
	}

	@Given("I have Booking Id")
	public void i_have_booking_id() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		bookingid = ReadJson.readJson(dataFilePath, "bookingid");
	}

	@When("I send request to get my booking details")
	public void i_send_request_to_get_my_booking_details() {
		System.out.println("Fetching details for Booking Id : "+bookingid);
		response = given()
				.pathParam("bookingId", bookingid)
				.when().get("/booking/{bookingId}")
				.then()
				.extract()
				.response();
	}

	@Then("I should see my booking details")
	public void i_should_see_my_booking_details() {
		System.out.println("Response : "+response.asString());
		int status = response.getStatusCode();
		Assert.assertEquals(200, status);
	}

	@When("I send request to cancel my Booking")
	public void i_send_request_to_cancel_my_booking() {
		System.out.println("Cancelling booking for Booking Id : "+bookingid);
		String token = "token="+tokenCode;	

		response = given()
		.header("Content-Type", "application/json")
		.header("Cookie",token)
		.contentType(ContentType.JSON)
		.pathParam("bookingId", bookingid)
		.when()
		.delete("/booking/{bookingId}")
		.then()
		.extract()
		.response();
	}

	@Then("I should see my booking is cancelled")
	public void i_should_see_my_booking_is_cancelled() {
		int status = response.getStatusCode();
		Assert.assertEquals(201, status);
	}
	
	@Then("I should not see my booking details")
	public void i_should_not_see_my_booking_details() {
		int status = response.getStatusCode();
		Assert.assertEquals(404, status);
	}
	



}

