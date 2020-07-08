package tk.tcomad.unibot.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import tk.tcomad.unibot.service.BotRunnerService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentsResourceTest {

  @LocalServerPort
  private int port;

  @MockBean
  private BotRunnerService botRunnerService;

  private Header authorizationHeader;
  private Header foreignAuthorizationHeader;

  @Before
  public void setUp() {
    RestAssured.port = port;
    authorizationHeader = RestUtils.getAuthHeader();
    foreignAuthorizationHeader = RestUtils.getForeignHeader();
  }

  @Test
  public void shouldAdd() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");

    Integer createdObjectId = RestUtils.postObject("/students", authorizationHeader, create);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students/" + createdObjectId)
        .then()
        .statusCode(200)
        .body("name", equalTo("1"))
        .body("data", equalTo("2"))
        .contentType(ContentType.JSON);
  }

  @Test
  public void shouldList() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");

    RestUtils.postObject("/students", authorizationHeader, create);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students")
        .then()
        .statusCode(200)
        .body("_embedded.students.name[0]", equalTo("1"))
        .body("_embedded.students.data[0]", equalTo("2"));
  }

  @Test
  public void shouldNotListForeign() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");

    RestUtils.postObject("/students", authorizationHeader, create);

    given().contentType(ContentType.JSON)
        .header(foreignAuthorizationHeader)
        .when()
        .get("/students")
        .then()
        .statusCode(200)
        .body("_embedded.students", hasSize(0));
  }

  @Test
  public void shouldUpdate() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");
    Integer createdObjectId = RestUtils.postObject("/students", authorizationHeader, create);

    JsonObject update = new JsonObject();
    update.addProperty("name", "3");
    update.addProperty("data", "4");

    given().contentType(ContentType.JSON)
        .body(update.toString())
        .header(authorizationHeader)
        .when()
        .put("/students/" + createdObjectId)
        .then()
        .statusCode(200);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students/" + createdObjectId)
        .then()
        .statusCode(200)
        .body("name", equalTo("3"))
        .body("data", equalTo("4"));
  }

  @Test
  public void shouldNotUpdateForeign() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");
    Integer createdObjectId = RestUtils.postObject("/students", authorizationHeader, create);

    JsonObject update = new JsonObject();
    update.addProperty("name", "3");
    update.addProperty("data", "4");

    given().contentType(ContentType.JSON)
        .body(update.toString())
        .header(foreignAuthorizationHeader)
        .when()
        .put("/students/" + createdObjectId)
        .then()
        .statusCode(403);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students/" + createdObjectId)
        .then()
        .statusCode(200)
        .body("name", equalTo("1"))
        .body("data", equalTo("2"));
  }

  @Test
  public void shouldDelete() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");
    Integer createdObjectId = RestUtils.postObject("/students", authorizationHeader, create);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .delete("/students/" + createdObjectId)
        .then()
        .statusCode(204);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students/" + createdObjectId)
        .then()
        .statusCode(404);
  }

  @Test
  public void shouldNotDeleteForeign() {
    JsonObject create = new JsonObject();
    create.addProperty("name", "1");
    create.addProperty("data", "2");
    Integer createdObjectId = RestUtils.postObject("/students", authorizationHeader, create);

    given().contentType(ContentType.JSON)
        .header(foreignAuthorizationHeader)
        .when()
        .delete("/students/" + createdObjectId)
        .then()
        .statusCode(403);

    given().contentType(ContentType.JSON)
        .header(authorizationHeader)
        .when()
        .get("/students/" + createdObjectId)
        .then()
        .statusCode(200)
        .body("name", equalTo("1"))
        .body("data", equalTo("2"));
  }
}