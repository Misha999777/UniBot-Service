package tk.tcomad.unibot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't start bot")
public class BotException extends RuntimeException {

  public BotException(String reason) {
    super(reason);
  }
}
