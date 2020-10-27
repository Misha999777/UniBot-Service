package tk.tcomad.unibot.controller;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.payload.BotRequest;
import tk.tcomad.unibot.payload.BotResponse;
import tk.tcomad.unibot.payload.WeekRequest;
import tk.tcomad.unibot.repository.BotRepository;
import tk.tcomad.unibot.service.BotRunnerService;

@RestController
@CrossOrigin
@AllArgsConstructor
public class BotController {

    private final BotRepository botRepository;
    private final TelegramConfig telegramConfig;
    private final BotRunnerService botRunnerService;

    @PostMapping("/register")
    public ResponseEntity<BotResponse> welcome(@RequestBody BotRequest botRequest) {
        if (!botRepository.existsByApi(botRequest.getApi())) {
            Bot bot = new Bot(botRequest.getApi(), botRequest.getUsername(), telegramConfig.getInstanceId());
            botRunnerService.start(bot);
            bot.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
            botRepository.save(bot);
            return ResponseEntity.status(HttpStatus.OK).body(new BotResponse(bot.getId(), botRequest.getUsername()));
        } else {
            Bot bot = botRepository.findByApi(botRequest.getApi());
            return ResponseEntity.status(HttpStatus.OK).body(new BotResponse(bot.getId(), botRequest.getUsername()));
        }
    }

    @PostMapping("/setWeek")
    public ResponseEntity<BotResponse> setWeek(@RequestBody WeekRequest weekRequest) {
        Optional<Bot> bot = botRepository.findById(weekRequest.getBotId());
        if (bot.isPresent()) {
            bot.get().setWeekStart(weekRequest.getDate());
            botRepository.save(bot.get());
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new BotResponse(bot.get().getId(), bot.get().getUsername()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
