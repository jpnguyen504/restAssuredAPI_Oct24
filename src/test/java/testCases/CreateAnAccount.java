package testCases;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateAnAccount extends GenerateBearerToken{

	String baseUri;
	String createEndpoint;
	String contentTypeHeader;
	String username;
	String password;
	String createBodyFilePath;
	
	String responseBody;
	JsonPath jp;
	
	String readAllEndpoint;
	String firstAccountId;
	
	String readOneEndpoint;
	
	public CreateAnAccount() {
		baseUri = getProp("baseURI");
		createEndpoint = getProp("createEndpoint");
		contentTypeHeader = getProp("contentType");
		username = getProp("basicAuthUser");
		password = getProp("basicAuthPassw");
		createBodyFilePath = "src\\main\\java\\data\\createPayload.json";
		
		readAllEndpoint = getProp("readAllEndpoint");
		readOneEndpoint = getProp("readOneEndpoint");
	}
	
	@Test (priority = 1)
	public void createAnAccount() {
		
		Response resp =
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.body(new File(createBodyFilePath)).
		when()
			.post(createEndpoint).
		then()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, 201, "Wrong Status Code");
		
		responseBody = resp.getBody().asString();
		jp = new JsonPath(responseBody);
		Assert.assertEquals(jp.getString("message"), "Account created successfully.", "Wrong response body");
	}
	
	@Test (priority = 2)
	public void readAllProducts() {
		
		Response resp = 
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.auth().preemptive().basic(username, password)
			.log().all().
		when()
			.get(readAllEndpoint).
		then()
			.log().all()
			.extract().response();
		
		String responseBody = resp.getBody().asString();
//		System.out.println(responseBody);
		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.getString("records[0].account_id");
	}
	
	@Test (priority = 3)
	public void readOneProduct() {
		Response resp = 
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.queryParam("account_id", firstAccountId)
			.log().all().
		when()
			.get(readOneEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Wrong Status Code");
		
		//validate responseTime
		
		String responseBody = resp.getBody().asString();
		jp = new JsonPath(responseBody);
		String actualAccountName = jp.getString("account_name");
		String actualAccountNumber = jp.getString("account_number");
		String actualDescription = jp.getString("description");
		String actualBalance = jp.getString("balance");
		String actualContactPerson = jp.getString("contact_person");
		
		JsonPath jp2 = new JsonPath(new File(createBodyFilePath));
		String expectedAccountName = jp2.getString("account_name");
		String expectedAccountNumber = jp2.getString("account_number");
		String expectedDescription = jp2.getString("description");
		String expectedBalance = jp2.getString("balance");
		String expectedContactPerson = jp2.getString("contact_person");
		
		Assert.assertEquals(actualAccountName, expectedAccountName, "Wrong account name");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Wrong account number");
		Assert.assertEquals(actualDescription, expectedDescription, "Wrong description");
		Assert.assertEquals(actualBalance, expectedBalance, "Wrong balance");
		Assert.assertEquals(actualContactPerson, expectedContactPerson, "Wrong contact");
	}
}
