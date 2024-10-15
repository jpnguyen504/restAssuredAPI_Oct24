package testCases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct extends GenerateBearerToken{
	
	String baseUri;
	String readOneEndpoint;
	String contentTypeHeader;
	String username;
	String password;
	
	public ReadOneProduct() {
		baseUri = getProp("baseURI");
		readOneEndpoint = getProp("readOneEndpoint");
		contentTypeHeader = getProp("contentType");
		username = getProp("basicAuthUser");
		password = getProp("basicAuthPassw");
		
	}
	
	@Test
	public void readOneProduct() {
		Response resp = 
		given()
			.baseUri(baseUri)
			.header("Content-Type", contentTypeHeader)
			.header("Authorization", "Bearer " + generateBearer())
			.queryParam("account_id", "998")
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
		JsonPath jp = new JsonPath(responseBody);
		String actualAccountName = jp.getString("account_name");
		String actualAccountNumber = jp.getString("account_number");
		String actualDescription = jp.getString("description");
		String actualBalance = jp.getString("balance");
		String actualContactPerson = jp.getString("contact_person");
		
		Assert.assertEquals(actualAccountName, "MR_Acc100", "Wrong account name");
		Assert.assertEquals(actualAccountNumber, "123010191", "Wrong account number");
		Assert.assertEquals(actualDescription, "MR_TestAcc50", "Wrong description");
		Assert.assertEquals(actualBalance, "1068.00", "Wrong balance");
		Assert.assertEquals(actualContactPerson, "MMR180", "Wrong contact");
	}
	
}
