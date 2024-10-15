package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.configReader;

public class GenerateBearerToken extends configReader{

	String baseUri;
	String authenticationEndpoint;
	String contentTypeHeader;
	String authorizationBodyFilePath;
	long responseTime;
	
	public GenerateBearerToken() {
		baseUri = getProp("baseURI");
		authenticationEndpoint = getProp("authenticationEndpoint");
		contentTypeHeader = getProp("contentType");
		authorizationBodyFilePath = "src\\main\\java\\data\\authorizationPayload.json";
	}
	
	public String generateBearer() {
		
		Response resp =	
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.body(new File(authorizationBodyFilePath)).
		when()
			.post(authenticationEndpoint).
		then()
			.extract().response();
		
		String responseBody = resp.getBody().asString();
		JsonPath jp = new JsonPath(responseBody);
		String bearer = jp.getString("access_token");
		return bearer;
	}
}
