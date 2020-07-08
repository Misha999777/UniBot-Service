package tk.tcomad.unibot.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class BotRequest {
  @Setter
  @Getter
  private String api;

  @Getter
  @Setter
  private String username;
}
