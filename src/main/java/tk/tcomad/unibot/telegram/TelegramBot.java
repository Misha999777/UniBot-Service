package tk.tcomad.unibot.telegram;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.BotUser;
import tk.tcomad.unibot.entity.botdata.*;
import tk.tcomad.unibot.repository.*;

public class TelegramBot extends AbilityBot {

    public static final String WELCOME_MESSAGE = "Добро пожаловать в Telegram бот группы, для справки - /help!";
    public static final String WRONG_COMMAND_MESSAGE = "Неверная команда";
    public static final String NO_ELEMENTS_MESSAGE = "Данные отсутствуют";
    public static final String SELECT_MESSAGE = "Выберите для подробностей";
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private static final String SLASH = "/";

    private static final int DAYS_IN_WEEK = 7;

    private final AppRepository appRepository;
    private final BookRepository bookRepository;
    private final LectureRepository lectureRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TelegramConfig telegramConfig;

    private final tk.tcomad.unibot.entity.Bot bot;

    public TelegramBot(AppRepository appRepository,
                       BookRepository bookRepository,
                       LectureRepository lectureRepository,
                       LessonRepository lessonRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       UserRepository userRepository,
                       TelegramConfig telegramConfig, Bot bot, DBContext dbContext) {
        super(bot.getApi(), bot.getUsername(), dbContext);
        this.appRepository = appRepository;
        this.bookRepository = bookRepository;
        this.lectureRepository = lectureRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.telegramConfig = telegramConfig;
        this.bot = bot;
        scheduleTimeTableSending();
    }

    @Override
    public int creatorId() {
        return telegramConfig.getCreatorId();
    }

    public void setSender(SilentSender silentSender) {
        silent = silentSender;
    }

    public Ability sendWelcome() {
        Map<String, String> commands = Arrays.stream(Command.values())
                                             .map(Command::getMessage)
                                             .collect(
                                                     LinkedHashMap::new,
                                                     (map, item) -> map.put(item.getLeft(), item.getRight()),
                                                     Map::putAll
                                                     );
        return Ability
                .builder()
                .name("start")
                .input(0)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> sendMap(ctx.chatId(), WELCOME_MESSAGE, commands))
                .build();
    }

    public Reply receiveCallback() {
        Consumer<Update> action = upd -> {
            if (!userRepository.existsByChatIDAndBot(getChatId(upd), bot)) {
                userRepository.save(new BotUser(getChatId(upd), this.bot));
            }
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            String id = upd.getCallbackQuery().getId();
            answer.setCallbackQueryId(id);
            silent.execute(answer);
            processCallback(getChatId(upd), upd.getCallbackQuery().getData());
        };
        return Reply.of(action, Flag.CALLBACK_QUERY);
    }

    private void processCallback(long chatId, String payload) {
        Optional<Command> optionalCommand = Command.fromMessage(payload);
        if (optionalCommand.isPresent()) {
            processCommand(chatId, optionalCommand.get());
        } else {
            String[] splitted = payload.split(StringUtils.SPACE);
            String callbackText = splitted[0];
            Long id = Long.parseLong(splitted[1]);
            Optional<Callback> optionalCallback = Callback.fromCallbackText(callbackText);
            optionalCallback.ifPresentOrElse(callback -> processDetail(chatId, id, callback),
                                             () -> silent.send(WRONG_COMMAND_MESSAGE, chatId));
        }
    }

    private void processCommand(Long chatId, Command payload) {
        switch (payload) {
            case TIMETABLE:
                Map<String, String> days = Arrays.stream(Day.values())
                                                 .map(Day::getMessage)
                                                 .collect(
                                                         LinkedHashMap::new,
                                                         (map, item) -> map.put(item.getLeft(), item.getRight()),
                                                         Map::putAll
                                                         );
                sendMap(chatId, SELECT_MESSAGE, days);
                break;
            case TEACHERS:
                sendList(chatId, teacherRepository.findAllByBot(bot));
                break;
            case APPS:
                sendList(chatId, appRepository.findAllByBot(bot));
                break;
            case STUDENTS:
                sendList(chatId, studentRepository.findAllByBot(bot));
                break;
            case LECTURES:
                sendList(chatId, lectureRepository.findAllByBot(bot));
                break;
            case BOOKS:
                sendList(chatId, bookRepository.findAllByBot(bot));
                break;
        }
    }

    private void processDetail(long chatId, Long id, Callback callback) {
        switch (callback) {
            case APP:
                App app = appRepository.findAppById(id)
                                       .orElseThrow(NoSuchElementException::new);
                sendMessageWithText(chatId, app.getInfo());
                break;
            case BOOK:
                Book book = bookRepository.findBookById(id)
                                          .orElseThrow(NoSuchElementException::new);
                sendMessageWithText(chatId, book.getInfo());
                break;
            case LECTURE:
                Lecture lecture = lectureRepository.findLectureById(id)
                                                   .orElseThrow(NoSuchElementException::new);
                sendMessageWithText(chatId, lecture.getInfo());
                break;
            case STUDENT:
                Student student = studentRepository.findStudentById(id)
                                                   .orElseThrow(NoSuchElementException::new);
                sendMessageWithText(chatId, student.getInfo());
                break;
            case TEACHER:
                Teacher teacher = teacherRepository.findTeacherById(id)
                                                   .orElseThrow(NoSuchElementException::new);
                sendMessageWithText(chatId, teacher.getInfo());
                break;
            case DAY:
                sendTimeTableDetails(chatId, id.intValue());
                break;
        }
    }

    private void sendTimeTableDetails(Long chatId, Integer day) {
        List<Lesson> lessons = lessonRepository.findAllByBotAndDay(bot, day);

        String timetable = lessons.stream()
                                  .map(lecture -> lecture.getWeekLesson(getWeek()))
                                  .collect(Collectors.joining(System.lineSeparator()));
        StringUtils.defaultIfEmpty(timetable, NO_ELEMENTS_MESSAGE);

        sendMessageWithText(chatId, timetable);
    }

    private void sendMap(Long chatId, String text, Map<String, String> map) {
        List<List<InlineKeyboardButton>> keyboard = map.entrySet().stream()
                                                       .map(command -> new InlineKeyboardButton()
                                                               .setText(command.getKey())
                                                               .setCallbackData(command.getValue()))
                                                       .map(List::of)
                                                       .collect(Collectors.toList());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup().setKeyboard(keyboard);

        sendMessageWithMarkup(chatId, text, markup);
    }

    private void sendList(Long chatId, List<? extends BotData> list) {
        if (list.isEmpty()) {
            sendMessageWithText(chatId, NO_ELEMENTS_MESSAGE);
        } else {
            sendMessageWithMarkup(chatId, TelegramBot.SELECT_MESSAGE, createMarkup(list));
        }
    }

    private void sendMessageWithText(Long chatId, String info) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(info);
        silent.execute(message);
    }

    private void sendMessageWithMarkup(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text)
                .setReplyMarkup(markup);

        silent.execute(message);
    }

    private InlineKeyboardMarkup createMarkup(Collection<? extends BotData> collection) {
        List<List<InlineKeyboardButton>> keyboard = collection.stream()
                                                              .map(element -> new InlineKeyboardButton()
                                                                      .setText(element.getName())
                                                                      .setCallbackData(SLASH + element.getClass()
                                                                                                      .getSimpleName() +
                                                                                               StringUtils.SPACE +
                                                                                               element.getId()))
                                                              .map(List::of)
                                                              .collect(Collectors.toList());

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    private boolean getWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate now = LocalDate.now(ZoneId.systemDefault());

        String dateStr = bot.getWeekStart() != null
                ? bot.getWeekStart()
                : now.format(formatter);
        LocalDate date = LocalDate.parse(dateStr, formatter);

        int weeks = Period.between(date, now).getDays() / DAYS_IN_WEEK;
        return weeks % 2 == 0;
    }

    private void scheduleTimeTableSending() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextRun = now.withHour(telegramConfig.getHour()).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new TimeTableSender(),
                                      initialDelay,
                                      TimeUnit.DAYS.toDays(1),
                                      TimeUnit.DAYS);
    }

    private class TimeTableSender implements Runnable {

        @Override
        public void run() {
            log.info("Sending timetable to users of bot {}", bot.getUsername());
            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
            List<Lesson> lessons = lessonRepository.findAllByBotAndDay(bot, now.getDayOfWeek().getValue());

            String timeTable = lessons.stream()
                                      .map(lecture -> lecture.getWeekLesson(getWeek()))
                                      .collect(Collectors.joining());

            if (lessons.isEmpty()) {
                userRepository.findAllByBot(bot).stream()
                              .map(user -> new SendMessage()
                                      .setChatId(user.getChatID())
                                      .setText(timeTable)
                                      .disableNotification())
                              .forEach(silent::execute);
            }
        }
    }
}
