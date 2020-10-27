package tk.tcomad.unibot.payload;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class BotResponse {

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String username;

    public BotResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
