import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import io.restassured.path.json.JsonPath;
import static org.junit.Assert.*;

public class ExampleApiTest {
    static String basicUrl = "http://coop.apps.symfonycasts.com/";
    static int userId = 4282;

    private static String getFullUrl(String endpoint) {
        return basicUrl + "api/" + userId + "/" + endpoint;
    }

    private static String getOrGenerateToken() {
        return "63f5face6a4a323ce80ae7e37363165b9bacd4a1";
    }

    @Test
    public void testPostBarnUnlock() {
        String token = getOrGenerateToken();
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("barn-unlock"));
        String messageExpected = "You just unlocked your barn! Watch out for strangers!";
        String secondMessageExpected = "The barn is already wide open! Let's throw a party!";

        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");
        String message = response.jsonPath().get("message");
        assertNotNull(message);
        assertTrue(message.equals(messageExpected) || message.equals(secondMessageExpected));
    }

    @Test
    public void testPostChickensFeed() {
        String token = getOrGenerateToken();
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("chickens-feed"));
        String messageExpected = "Your chickens are now full and happy";
        String secondMessageExpected = "You just fed them! Do you want them to explode??";

        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");

        String message = response.jsonPath().get("message");
        assertNotNull(message);
        assertTrue(message.equals(messageExpected) || message.equals(secondMessageExpected));
    }

    @Test
    public void testPostCollectEggs() {
        String token = getOrGenerateToken();
        Response response = RestAssured.given().header("Authorization", "Bearer " + token).post(getFullUrl("eggs-collect"));

        assertEquals(200, response.getStatusCode());
        assertEquals(response.getHeader("Content-Type"), "application/json");

        JsonPath json = response.jsonPath();

        if (json == null){
            System.out.println("AAA");
            return;
        }
        int data = json.getInt("data");
        String messageExpected = "Hey look at that, " + data + " eggs have been collected!";
        String secondMessageExpected = "Hey, give the ladies a break. Makin' eggs ain't easy!";

        String message = response.jsonPath().get("message");
        assertNotNull(message);



        if (data == 0) {
            assertEquals(message, secondMessageExpected);
            return;
        }

        assertEquals(message, messageExpected);
    }
}
