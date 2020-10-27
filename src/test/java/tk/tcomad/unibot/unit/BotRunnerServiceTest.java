package tk.tcomad.unibot.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.repository.*;
import tk.tcomad.unibot.service.BotRunnerService;

public class BotRunnerServiceTest {

    public static final String BOT1_API = "TestAPI";
    public static final String BOT1_USERNAME = "Test_bot";
    public static final String BOT1_INSTANCE_ID = "1";
    public static final String BOT2_API = "TestAPI";
    public static final String BOT2_USERNAME = "Test_bot";
    public static final String BOT2_INSTANCE_ID = "1";

    private BotRunnerService botRunnerService;
    private TelegramBotsApi telegramBotsApi;

    @Before
    public void setUp() {
        AppRepository appRepository = mock(AppRepository.class);
        BookRepository bookRepository = mock(BookRepository.class);
        LectureRepository lectureRepository = mock(LectureRepository.class);
        LessonRepository lessonRepository = mock(LessonRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        TeacherRepository teacherRepository = mock(TeacherRepository.class);

        UserRepository userRepository = mock(UserRepository.class);
        TelegramConfig telegramConfig = mock(TelegramConfig.class);
        when(telegramConfig.getInstanceId()).thenReturn("1");

        BotRepository botRepository = mock(BotRepository.class);
        List<Bot> bots = new ArrayList<>();
        bots.add(new Bot(BOT1_API, BOT1_USERNAME, BOT1_INSTANCE_ID));
        bots.add(new Bot(BOT2_API, BOT2_USERNAME, BOT2_INSTANCE_ID));
        when(botRepository.findAllByInstanceId("1")).thenReturn(bots);

        telegramBotsApi = mock(TelegramBotsApi.class);

        botRunnerService = new BotRunnerService(appRepository, bookRepository,
                                                botRepository,
                                                lectureRepository, lessonRepository, studentRepository,
                                                teacherRepository,
                                                userRepository, telegramConfig, telegramBotsApi);
    }

    @Test
    public void shouldStartBots() throws TelegramApiRequestException {
        botRunnerService.initialStart();
        ArgumentCaptor<LongPollingBot> peopleCaptor = ArgumentCaptor
                .forClass(LongPollingBot.class);
        Mockito.verify(telegramBotsApi, times(2))
               .registerBot(peopleCaptor.capture());
        List<LongPollingBot> longPollingBots = peopleCaptor.getAllValues();
        assertEquals(BOT1_USERNAME, longPollingBots.get(0).getBotUsername());
        assertEquals(BOT1_API, longPollingBots.get(0).getBotToken());
        assertEquals(BOT2_USERNAME, longPollingBots.get(1).getBotUsername());
        assertEquals(BOT2_API, longPollingBots.get(1).getBotToken());
    }
}
