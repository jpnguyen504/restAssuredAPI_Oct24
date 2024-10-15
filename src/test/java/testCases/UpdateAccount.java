package testCases;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateAccount extends GenerateBearerToken{
	String baseUri;
	String updateEndpoint;
	String contentTypeHeader;
	String username;
	String password;
	
	Map<String, String> updatedAccountInfo;
	
	String responseBody;
	JsonPath jp;
	
	String readOneEndpoint;
	
	String createBodyFilePath;
	
	public UpdateAccount() {
		baseUri = getProp("baseURI");
		updateEndpoint = getProp("updateEndpoint");
		contentTypeHeader = getProp("contentType");
		username = getProp("basicAuthUser");
		password = getProp("basicAuthPassw");
		updatedAccountInfo = new HashMap<String, String>();
		
		readOneEndpoint = getProp("readOneEndpoint");
		
		createBodyFilePath = "src\\main\\java\\data\\createPayload.json";
	}
	
	@Test (priority = 1)
	public void updateAccount() {
		
		Response resp =
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.body(updateMap())
			.log().all().
		when()
			.put(updateEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Wrong Status Code");
		
		responseBody = resp.getBody().asString();
		jp = new JsonPath(responseBody);
		Assert.assertEquals(jp.getString("message"), "Account updated successfully.", "Wrong response body");
	}
	
	@Test (priority = 2)
	public void readOneProduct() {
		Response resp = 
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.queryParam("account_id", "1000")
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
		
		String expectedAccountName = updateMap().get("account_name");
		String expectedAccountNumber = updateMap().get("account_number");
		String expectedDescription = updateMap().get("description");
		String expectedBalance = updateMap().get("balance");
		String expectedContactPerson = updateMap().get("contact_person");
		
		Assert.assertEquals(actualAccountName, expectedAccountName, "Wrong account name");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Wrong account number");
		Assert.assertEquals(actualDescription, expectedDescription, "Wrong description");
		Assert.assertEquals(actualBalance, expectedBalance, "Wrong balance");
		Assert.assertEquals(actualContactPerson, expectedContactPerson, "Wrong contact");
	}
	
	public Map<String, String> updateMap() {
		updatedAccountInfo.put("account_id", "1000");
		updatedAccountInfo.put("account_name", "JP Techfios account 607");
		updatedAccountInfo.put("account_number", "123456789");
		updatedAccountInfo.put("description", "Updated Description");
		updatedAccountInfo.put("balance", "100.22");
		updatedAccountInfo.put("contact_person", "JP");
		return updatedAccountInfo;
	}
}
