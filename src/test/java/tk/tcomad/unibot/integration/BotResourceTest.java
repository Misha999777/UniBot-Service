package tk.tcomad.unibot.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.integration.config.OAuth2TestClientConfig;
import tk.tcomad.unibot.integration.utils.RestResourceTestUtils;
import tk.tcomad.unibot.service.BotRunnerService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(OAuth2TestClientConfig.class)
public class BotResourceTest {

    private static final String BOT_USERNAME = "5";
    private static final String BOT_KEY = "6";

    @Autowired
    @Qualifier("restTemplateUser1")
    private OAuth2RestOperations oAuth2RestOperations1;
    @Autowired
    @Qualifier("restTemplateUser2")
    private OAuth2RestOperations oAuth2RestOperations2;
    @LocalServerPort
    private int port;
    @MockBean
    private BotRunnerService botRunnerService;
    @MockBean
    private TelegramConfig telegramConfig;

    private Header authorizationHeader1;
    private Header authorizationHeader2;

    @Before
    public void setUp() {
        RestAssured.port = port;
        Mockito.when(telegramConfig.getInstanceId()).thenReturn("1");
        authorizationHeader1 = new Header("Authorization", "Bearer " + oAuth2RestOperations1.getAccessToken().getValue());
        authorizationHeader2 = new Header("Authorization", "Bearer " + oAuth2RestOperations2.getAccessToken().getValue());
    }

    @Test
    public void shouldAdd() {
        RestResourceTestUtils.createBot(authorizationHeader1, BOT_KEY, BOT_USERNAME);
    }

    @Test
    public void shouldList() {
        RestResourceTestUtils.createBot(authorizationHeader1, BOT_KEY, BOT_USERNAME);
        given().contentType(ContentType.JSON)
               .header(authorizationHeader1)
               .when()
               .get("/bots")
               .then()
               .statusCode(200)
               .body("_embedded.bots.username", hasItem(BOT_USERNAME))
               .body("_embedded.bots.api", hasItem(BOT_KEY));
    }

    @Test
    public void shouldNotListForeign() {
        RestResourceTestUtils.createBot(authorizationHeader1, BOT_KEY, BOT_USERNAME);
        given().contentType(ContentType.JSON)
               .header(authorizationHeader2)
               .when()
               .get("/bots")
               .then()
               .statusCode(200)
               .body("_embedded.bots.username", not(hasItem(BOT_USERNAME)))
               .body("_embedded.bots.api", not(hasItem(BOT_KEY)));
    }

    @Test
    public void shouldSetWeek() {
        Integer id = RestResourceTestUtils.createBot(authorizationHeader1, BOT_KEY, BOT_USERNAME);
        JsonObject week = new JsonObject();
        week.addProperty("botId", id.toString());
        week.addProperty("date", "123");
        given().contentType(ContentType.JSON)
               .body(week.toString())
               .header(authorizationHeader1)
               .when()
               .post("/setWeek")
               .then()
               .statusCode(200);
    }

}
