package tk.tcomad.unibot.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.tcomad.unibot.config.TelegramConfig;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.payload.BotRequest;
import tk.tcomad.unibot.payload.JwtAuthenticationResponse;
import tk.tcomad.unibot.repository.BotRepository;
import tk.tcomad.unibot.security.JwtTokenProvider;
import tk.tcomad.unibot.service.BotRunnerService;

@RestController
@AllArgsConstructor
public class BotController {

    private final BotRepository botRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final BotRunnerService botRunnerService;
    private final TelegramConfig telegramConfig;

    @PostMapping("/auth")
    public ResponseEntity<JwtAuthenticationResponse> welcome(@RequestBody BotRequest botRequest) {
        if(!botRepository.existsByApi(botRequest.getApi())) {
            Bot bot = new Bot(botRequest.getApi(), botRequest.getUsername(), telegramConfig.getInstanceId());
            botRunnerService.start(bot);
            botRepository.save(bot);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                botRequest.getUsername(),
                botRequest.getApi()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(new JwtAuthenticationResponse(jwt, botRequest.getUsername()));
    }
}
