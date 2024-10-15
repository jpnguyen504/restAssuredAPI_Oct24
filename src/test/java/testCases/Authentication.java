package testCases;

import util.configReader;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Authentication extends configReader{

	String baseUri;
	String authenticationEndpoint;
	String contentTypeHeader;
	String authorizationBodyFilePath;
	long responseTime;
	
	public Authentication() {
		baseUri = getProp("baseURI");
		authenticationEndpoint = getProp("authenticationEndpoint");
		contentTypeHeader = getProp("contentType");
		authorizationBodyFilePath = "src\\main\\java\\data\\authorizationPayload.json";
	}
	
	@Test	//Test annotation only works if its public void
	public void generateBearer() {
		
		Response resp =	
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.body(new File(authorizationBodyFilePath)).
		when()
			.post(authenticationEndpoint).
		then()
			.extract().response();
		
		int statusCode = resp.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, 201, "Wrong Status Code");
		
		String responseBody = resp.getBody().asString();
		System.out.println("Response Body: " + responseBody);
		JsonPath jp = new JsonPath(responseBody);
		System.out.println("Bearer Token: " + jp.getString("access_token"));
		
		responseTime = resp.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: " + responseTime + "ms");
		Assert.assertEquals(validateResponseTime(), true, "Response Time not within range");
	}
	
	public boolean validateResponseTime() {
		boolean withinRange = false;
		if(responseTime <= 2000) {
			withinRange = true;
		}
		return withinRange;
	}
	
}
