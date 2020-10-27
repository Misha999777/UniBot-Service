package tk.tcomad.unibot.integration.utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.util.UUID;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.Builder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

@Builder(builderClassName = "Builder")
public class RestResourceTestUtils {

    private static final String BOT1_USERNAME = "1";
    private static final String BOT1_KEY = "2";
    private static final String BOT2_USERNAME = "3";
    private static final String BOT2_KEY = "4";

    private final Header authHeaderForUser1;
    private final Header authHeaderForUser2;
    private final Integer idOfUser1Bot;
    private final Integer idOfUser2Bot;
    private final String entityTitleName;
    private final String entitySubtitleName;
    private final String entityUri;

    public static RestResourceTestUtils.Builder builder(OAuth2RestOperations user1Client,
                                                        OAuth2RestOperations user2Client) {
        Header authorizationHeaderOfUser1 =
                new Header("Authorization", "Bearer " + user1Client.getAccessToken().getValue());
        Header authorizationHeaderOfUser2 =
                new Header("Authorization", "Bearer " + user2Client.getAccessToken().getValue());
        Integer idOfUser1Bot = RestResourceTestUtils.createBot(authorizationHeaderOfUser1, BOT1_KEY,BOT1_USERNAME);
        Integer idOfUser2Bot = RestResourceTestUtils.createBot(authorizationHeaderOfUser2, BOT2_KEY, BOT2_USERNAME);
        return new Builder()
                .authHeaderForUser1(authorizationHeaderOfUser1)
                .authHeaderForUser2(authorizationHeaderOfUser2)
                .idOfUser1Bot(idOfUser1Bot)
                .idOfUser2Bot(idOfUser2Bot);
    }

    public static Integer createBot(Header header, String api, String username) {
        JsonObject bot = new JsonObject();
        bot.addProperty("username", username);
        bot.addProperty("api", api);
        return given().contentType(ContentType.JSON)
                      .body(bot.toString())
                      .header(header)
                      .when()
                      .post("/register")
                      .then()
                      .statusCode(200)
                      .extract().path("id");
    }

    public void shouldAdd() {
        String param1 = UUID.randomUUID().toString();
        String param2 = UUID.randomUUID().toString();
        Integer createdObjectId = createEntity(param1, param2);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(200)
               .body(entityTitleName, equalTo(param1))
               .body(entitySubtitleName, equalTo(param2))
               .contentType(ContentType.JSON);
    }

    public void shouldNotAddForeign() {
        Integer createdObjectId = createEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser2)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(403);
    }

    public void shouldList() {
        String param1 = UUID.randomUUID().toString();
        String param2 = UUID.randomUUID().toString();
        createEntity(param1, param2);
        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/bots/" + idOfUser1Bot + "/" + entityUri)
               .then()
               .statusCode(200)
               .body("_embedded." + entityUri + "." + entityTitleName, hasItem(param1))
               .body("_embedded." + entityUri + "." + entitySubtitleName, hasItem(param2));
    }

    public void shouldNotListForeign() {
        createEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        given().contentType(ContentType.JSON)
               .header(authHeaderForUser2)
               .when()
               .get("/bots/" + idOfUser2Bot + "/" + entityUri)
               .then()
               .statusCode(200)
               .body("_embedded." + entityUri, hasSize(0));
    }

    public void shouldNotAccessForeign() {
        Integer created = createEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        given().contentType(ContentType.JSON)
               .header(authHeaderForUser2)
               .when()
               .get("/" + entityUri + "/" + created)
               .then()
               .statusCode(403);
    }

    public void shouldUpdate() {
        Integer createdObjectId = createEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        String param3 = UUID.randomUUID().toString();
        String param4 = UUID.randomUUID().toString();
        JsonObject update = new JsonObject();
        update.addProperty(entityTitleName, param3);
        update.addProperty(entitySubtitleName, param4);

        given().contentType(ContentType.JSON)
               .body(update.toString())
               .header(authHeaderForUser1)
               .when()
               .put("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(200);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(200)
               .body(entityTitleName, equalTo(param3))
               .body(entitySubtitleName, equalTo(param4));
    }

    public void shouldNotUpdateForeign() {
        String param1 = UUID.randomUUID().toString();
        String param2 = UUID.randomUUID().toString();
        Integer createdObjectId = createEntity(param1, param2);

        JsonObject update = new JsonObject();
        update.addProperty(entityTitleName, UUID.randomUUID().toString());
        update.addProperty(entitySubtitleName, UUID.randomUUID().toString());

        given().contentType(ContentType.JSON)
               .body(update.toString())
               .header(authHeaderForUser2)
               .when()
               .put("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(403);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(200)
               .body(entityTitleName, equalTo(param1))
               .body(entitySubtitleName, equalTo(param2));
    }

    public void shouldDelete() {
        Integer createdObjectId = createEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .delete("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(204);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(404);
    }

    public void shouldNotDeleteForeign() {
        String param1 = UUID.randomUUID().toString();
        String param2 = UUID.randomUUID().toString();
        Integer createdObjectId = createEntity(param1, param2);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser2)
               .when()
               .delete("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(403);

        given().contentType(ContentType.JSON)
               .header(authHeaderForUser1)
               .when()
               .get("/" + entityUri + "/" + createdObjectId)
               .then()
               .statusCode(200)
               .body(entityTitleName, equalTo(param1))
               .body(entitySubtitleName, equalTo(param2));
    }

    private Integer createEntity(String param1, String param2) {
        JsonObject create = new JsonObject();
        create.addProperty(entityTitleName, param1);
        create.addProperty(entitySubtitleName, param2);
        create.addProperty("bot", "/bots/" + idOfUser1Bot);
        return given().contentType(ContentType.JSON)
                      .body(create.toString())
                      .header(authHeaderForUser1)
                      .when()
                      .post("/" + entityUri)
                      .then()
                      .statusCode(201)
                      .extract().path("id");
    }
}
