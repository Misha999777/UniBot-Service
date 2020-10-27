package tk.tcomad.unibot.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.MapDBContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.exception.BotException;
import tk.tcomad.unibot.repository.*;
import tk.tcomad.unibot.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
public class BotRunnerService {

    private static final Logger log = LoggerFactory.getLogger(BotRunnerService.class);

    private final AppRepository appRepository;
    private final BookRepository bookRepository;
    private final BotRepository botRepository;
    private final LectureRepository lectureRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TelegramConfig telegramConfig;
    private final TelegramBotsApi botsApi;

    @EventListener(ApplicationReadyEvent.class)
    public void initialStart() {
        List<Bot> bots = botRepository.findAllByInstanceId(telegramConfig.getInstanceId());

        log.info("Starting bots...");
        log.info("Instance ID: {}", telegramConfig.getInstanceId());
        log.info("Number of bots {}", bots.size());

        bots.forEach((bot) -> {
            try {
                this.start(bot);
            } catch (BotException exception) {
                log.warn("Skipping bot {}. Reason: {}", bot.getUsername(), exception.getMessage());
            }
        });
    }

    public void start(tk.tcomad.unibot.entity.Bot botToStart) {
        DB db = DBMaker.tempFileDB().closeOnJvmShutdown().transactionEnable().make();
        DBContext dbContext = new MapDBContext(db);
        TelegramBot bot = new TelegramBot(appRepository, bookRepository,
                                          lectureRepository, lessonRepository, studentRepository,
                                          teacherRepository, userRepository, telegramConfig,
                                          botToStart, dbContext);

        try {
            botsApi.registerBot(bot);
            log.info("Bot {} started", botToStart.getUsername());
        } catch (TelegramApiRequestException e) {
            throw new BotException(e.getMessage());
        }
    }
}
