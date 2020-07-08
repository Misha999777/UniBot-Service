package tk.tcomad.unibot.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.MapDBContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.repository.AppRepository;
import tk.tcomad.unibot.repository.BookRepository;
import tk.tcomad.unibot.repository.LectureRepository;
import tk.tcomad.unibot.repository.LessonRepository;
import tk.tcomad.unibot.repository.StudentRepository;
import tk.tcomad.unibot.repository.TeacherRepository;
import tk.tcomad.unibot.repository.UserRepository;
import tk.tcomad.unibot.repository.WeekRepository;
import tk.tcomad.unibot.telegram.Day;
import tk.tcomad.unibot.telegram.TelegramBot;

public class TelegramBotTest {
  public static final int USER_ID = 1337;
  public static final long CHAT_ID = 1337L;
  public static final long BOT_ID = 1L;
  public static final String BOT_API = "TestAPI";
  public static final String BOT_USERNAME = "Test_bot";
  public static final String BOT_INSTANCE_ID = "1";

  private TelegramBot bot;
  private SilentSender silent;

  @Before
  public void setUp() {
    Bot botEntity = new Bot(BOT_ID, BOT_API, BOT_USERNAME, BOT_INSTANCE_ID);

    AppRepository appRepository = TestUtils.mockAppRepository(botEntity);
    BookRepository bookRepository = TestUtils.mockBookRepository(botEntity);
    LectureRepository lectureRepository = TestUtils.mockLectureRepository(botEntity);
    LessonRepository lessonRepository = TestUtils.mockLessonRepository(botEntity);
    StudentRepository studentRepository = TestUtils.mockStudentRepository(botEntity);
    TeacherRepository teacherRepository = TestUtils.mockTeacherRepository(botEntity);
    WeekRepository weekRepository = TestUtils.mockWeekRepository(botEntity);

    UserRepository userRepository = mock(UserRepository.class);
    TelegramConfig telegramConfig = mock(TelegramConfig.class);

    silent = mock(SilentSender.class);

    DB db = DBMaker.tempFileDB()
        .closeOnJvmShutdown()
        .transactionEnable()
        .make();
    DBContext dbContext = new MapDBContext(db);

    bot = new TelegramBot(appRepository, bookRepository, lectureRepository,
        lessonRepository, studentRepository, teacherRepository, userRepository,
        weekRepository, telegramConfig, botEntity, dbContext);
    bot.setSender(silent);
  }

  @Test
  public void canSayHelloWorld() {
    Update upd = new Update();
    User user = new User(USER_ID, "Test", false, "User", "TestUser123", null);
    MessageContext context = MessageContext.newContext(upd, user, CHAT_ID);
    bot.sendWelcome().action().accept(context);
    ArgumentCaptor<SendMessage> peopleCaptor = ArgumentCaptor.forClass(SendMessage.class);
    Mockito.verify(silent, times(1)).execute(peopleCaptor.capture());

    assertEquals(TelegramBot.WELCOME_MESSAGE, peopleCaptor.getValue().getText());
  }

  @Test
  public void canListDays() {
    Update update = TestUtils.mockCallBackUpdate("/gettimetable");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();

    Map<String, String> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toMap(InlineKeyboardButton::getText, InlineKeyboardButton::getCallbackData));
    Map<String, String> expectedDays = Arrays.stream(Day.values())
        .collect(Collectors.toMap(day -> day.getMessage().getLeft(), day -> day.getMessage().getRight()));

    assertEquals(expectedDays, buttons);
  }

  @Test
  public void canShowDetailsOnLesson() {
    Update update = TestUtils.mockCallBackUpdate("/Day 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("TestLesson", captor.getValue().getText());
  }

  @Test
  public void canListBooks() {
    Update update = TestUtils.mockCallBackUpdate("/getbooks");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();
    List<InlineKeyboardButton> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    assertEquals("TestBook", buttons.get(0).getText());
    assertEquals("/Book 1", buttons.get(0).getCallbackData());
  }

  @Test
  public void canShowDetailsOnBook() {
    Update update = TestUtils.mockCallBackUpdate("/Book 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("https://test.com/book", captor.getValue().getText());
  }

  @Test
  public void canListLectures() {
    Update update = TestUtils.mockCallBackUpdate("/getlectures");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();
    List<InlineKeyboardButton> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    assertEquals("TestLecture", buttons.get(0).getText());
    assertEquals("/Lecture 1", buttons.get(0).getCallbackData());
  }

  @Test
  public void canShowDetailsOnLecture() {
    Update update = TestUtils.mockCallBackUpdate("/Lecture 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("https://test.com/lecture", captor.getValue().getText());
  }

  @Test
  public void canListApps() {
    Update update = TestUtils.mockCallBackUpdate("/getapps");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();
    List<InlineKeyboardButton> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    assertEquals("TestApp", buttons.get(0).getText());
    assertEquals("/App 1", buttons.get(0).getCallbackData());
  }

  @Test
  public void canShowDetailsOnApp() {
    Update update = TestUtils.mockCallBackUpdate("/App 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("https://test.com/app", captor.getValue().getText());
  }

  @Test
  public void canListStudents() {
    Update update = TestUtils.mockCallBackUpdate("/getstudents");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();
    List<InlineKeyboardButton> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    assertEquals("TestStudent", buttons.get(0).getText());
    assertEquals("/Student 1", buttons.get(0).getCallbackData());
  }

  @Test
  public void canShowDetailsOnStudent() {
    Update update = TestUtils.mockCallBackUpdate("/Student 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("+38000000000", captor.getValue().getText());
  }

  @Test
  public void canListTeachers() {
    Update update = TestUtils.mockCallBackUpdate("/getteachers");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals(TelegramBot.SELECT_MESSAGE, captor.getValue().getText());

    InlineKeyboardMarkup markup = (InlineKeyboardMarkup)captor.getValue().getReplyMarkup();
    List<InlineKeyboardButton> buttons = markup.getKeyboard().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

    assertEquals("TestTeacher", buttons.get(0).getText());
    assertEquals("/Teacher 1", buttons.get(0).getCallbackData());
  }

  @Test
  public void canShowDetailsOnTeacher() {
    Update update = TestUtils.mockCallBackUpdate("/Teacher 1");
    bot.receiveCallback().action.accept(update);
    ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);

    Mockito.verify(silent, times(2)).execute(captor.capture());

    assertEquals("+38000000000", captor.getValue().getText());
  }
}