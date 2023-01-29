import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import io.restassured.path.json.JsonPath;

import static org.junit.Assert.*;

public class CoopApiTest {
    static String basicUrl = "http://coop.apps.symfonycasts.com/";
    static int userId = 4282;

    private static String getFullUrl(String endpoint) {
        return basicUrl + "api/" + userId + "/" + endpoint;
    }

    private static String getOrGenerateToken() {
        return "77b0cfc926ca0256a3700a20751f3f092e4ecd4f";
    }

    @Test
    public void testPostBarnUnlock() {
        //Arrange
        String token = getOrGenerateToken();
        String messageExpected = "You just unlocked your barn! Watch out for strangers!";
        String secondMessageExpected = "The barn is already wide open! Let's throw a party!";

        //Act
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("barn-unlock"));

        //Asserts
        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");
        String message = response.jsonPath().get("message");
        assertNotNull(message);
        assertTrue(message.equals(messageExpected) || message.equals(secondMessageExpected));
    }

    @Test
    public void testPostChickensFeed() {
        //Arrange
        String token = getOrGenerateToken();
        String messageExpected = "Your chickens are now full and happy";
        String secondMessageExpected = "You just fed them! Do you want them to explode??";

        //Act
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("chickens-feed"));

        //Asserts
        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");
        String message = response.jsonPath().get("message");
        assertNotNull(message);
        assertTrue(message.equals(messageExpected) || message.equals(secondMessageExpected));
    }

    @Test
    public void testPostCollectEggs() {
        //Arrange
        String token = getOrGenerateToken();
        String secondMessageExpected = "Hey, give the ladies a break. Makin' eggs ain't easy!";

        //Act
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("eggs-collect"));

        //Asserts
        String message = response.jsonPath().get("message");
        String dataNoINT = response.jsonPath().getString("data");
        if (dataNoINT == null) {
            assertEquals(message, secondMessageExpected);
            return;
        }
        int data = response.jsonPath().getInt("data");
        String messageExpected = "Hey look at that, " + data + " eggs have been collected!";
        assertNotNull(message);
        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");
        assertEquals(message, messageExpected);
    }
}
