package tk.tcomad.unibot.integration;

import static io.restassured.RestAssured.given;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.http.Header;

public class RestUtils {

  private static final String TEST_BOT_1 = "Test_bot";
  private static final String TEST_API_1 = "TestAPI";
  private static final String TEST_BOT_2 = "Test_bot1";
  private static final String TEST_API_2 = "TestAPI1";

  public static Integer postObject(String path, Header authorizationHeader, JsonObject createObject) {
    Integer createdObjectId = given().contentType(ContentType.JSON)
        .body(createObject.toString())
        .header(authorizationHeader)
        .when()
        .post(path)
        .then()
        .statusCode(201)
        .extract().path("id");
    return createdObjectId;
    }

    public static Header getAuthHeader() {
      JsonObject bot = new JsonObject();
      bot.addProperty("username", TEST_BOT_1);
      bot.addProperty("api", TEST_API_1);
      String accessToken = given().contentType(ContentType.JSON)
          .body(bot.toString())
          .when()
          .post("/auth")
          .then()
          .statusCode(200)
          .extract().path("accessToken");
      return new Header("Authorization", "Bearer " + accessToken);
    }

  public static Header getForeignHeader() {
    JsonObject bot = new JsonObject();
    bot.addProperty("username", TEST_BOT_2);
    bot.addProperty("api", TEST_API_2);
    String accessToken = given().contentType(ContentType.JSON)
        .body(bot.toString())
        .when()
        .post("/auth")
        .then()
        .statusCode(200)
        .extract().path("accessToken");
    return new Header("Authorization", "Bearer " + accessToken);
  }
}
