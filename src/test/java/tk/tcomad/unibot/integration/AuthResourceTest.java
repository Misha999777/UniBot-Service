package tk.tcomad.unibot.integration;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tk.tcomad.unibot.service.BotRunnerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthResourceTest {

  @LocalServerPort
  int port;

  @MockBean
  private BotRunnerService botRunnerService;

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  public void shouldRequireAuth() {

    given().contentType(ContentType.JSON)
        .when()
        .post("/apps")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/books")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/lectures")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/lessons")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/students")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/teachers")
        .then()
        .statusCode(401);
    given().contentType(ContentType.JSON)
        .when()
        .post("/weeks")
        .then()
        .statusCode(401);
  }
}