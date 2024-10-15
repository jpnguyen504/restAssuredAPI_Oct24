package testCases;

import static io.restassured.RestAssured.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteAccount extends GenerateBearerToken{

	String baseUri;
	String deleteEndpoint;
	String contentTypeHeader;
	String username;
	String password;
	
	String responseBody;
	JsonPath jp;
	
	String readOneEndpoint;
	
	String createBodyFilePath;
	
	public DeleteAccount() {
		baseUri = getProp("baseURI");
		deleteEndpoint = getProp("deleteEndpoint");
		contentTypeHeader = getProp("contentType");
		username = getProp("basicAuthUser");
		password = getProp("basicAuthPassw");
		
		readOneEndpoint = getProp("readOneEndpoint");
	}
	
	@Test (priority = 1)
	public void deleteAccount() {
		Response resp =
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.queryParam("account_id", "999")
			.log().all().
		when()
			.delete(deleteEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Wrong Status Code");
		
		responseBody = resp.getBody().asString();
		jp = new JsonPath(responseBody);
		Assert.assertEquals(jp.getString("message"), "Account deleted successfully.", "Wrong Status Code");
	}
	@Test (priority = 2)
	public void readOneProduct() {
		Response resp = 
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.queryParam("account_id", "999")
			.log().all().
		when()
			.get(readOneEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 404, "Wrong Status Code");
		
		responseBody = resp.getBody().asString();
		jp = new JsonPath(responseBody);
		Assert.assertEquals(jp.getString("message"), "No Record Found", "Account Not Deleted");
	}
}
