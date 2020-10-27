package tk.tcomad.unibot.integration;

import io.restassured.RestAssured;
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
public class BooksResourceTest {

    @Autowired
    @Qualifier("restTemplateUser1")
    private OAuth2RestOperations clientOfUser1;
    @Autowired
    @Qualifier("restTemplateUser2")
    private OAuth2RestOperations clientOfUser2;
    @LocalServerPort
    private int port;
    @MockBean
    private BotRunnerService botRunnerService;
    @MockBean
    private TelegramConfig telegramConfig;

    private RestResourceTestUtils restResourceTestUtils;

    @Before
    public void setUp() {
        RestAssured.port = port;
        Mockito.when(telegramConfig.getInstanceId()).thenReturn("1");
        restResourceTestUtils = RestResourceTestUtils.builder(clientOfUser1, clientOfUser2)
                                                     .entityUri("books")
                                                     .entityTitleName("name")
                                                     .entitySubtitleName("url")
                                                     .build();
    }

    @Test
    public void shouldAdd() {
        restResourceTestUtils.shouldAdd();
    }

    @Test
    public void shouldNotAddForeign() {
        restResourceTestUtils.shouldNotAddForeign();
    }

    @Test
    public void shouldList() {
        restResourceTestUtils.shouldList();
    }

    @Test
    public void shouldNotListForeign() {
        restResourceTestUtils.shouldNotListForeign();
    }

    @Test
    public void shouldNotAccessForeign(){restResourceTestUtils.shouldNotAccessForeign();}

    @Test
    public void shouldUpdate() {
        restResourceTestUtils.shouldUpdate();
    }

    @Test
    public void shouldNotUpdateForeign() {
        restResourceTestUtils.shouldNotUpdateForeign();
    }

    @Test
    public void shouldDelete() {
        restResourceTestUtils.shouldDelete();
    }

    @Test
    public void shouldNotDeleteForeign() {
        restResourceTestUtils.shouldNotDeleteForeign();
    }
}
