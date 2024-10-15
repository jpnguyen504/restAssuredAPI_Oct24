package testCases;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadAllProducts extends Authentication{

	String baseUri;
	String readAllEndpoint;
	String contentTypeHeader;
	String username;
	String password;
	
	
	public ReadAllProducts() {
		baseUri = getProp("baseURI");
		readAllEndpoint = getProp("readAllEndpoint");
		contentTypeHeader = getProp("contentType");
		username = getProp("basicAuthUser");
		password = getProp("basicAuthPassw");
	}
	
	@Test
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
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Wrong Status Code");
		
		responseTime = resp.timeIn(TimeUnit.MILLISECONDS);
		Assert.assertEquals(validateResponseTime(), true, "Response Time not within range");
		
		String responseBody = resp.getBody().asString();
//		System.out.println(responseBody);
		JsonPath jp = new JsonPath(responseBody);
		System.out.println(jp.getString("records[0].account_id"));
		
	}
}