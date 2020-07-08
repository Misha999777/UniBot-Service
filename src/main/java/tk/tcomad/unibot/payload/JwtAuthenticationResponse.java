package tk.tcomad.unibot.payload;

import lombok.Getter;
import lombok.Setter;

public class JwtAuthenticationResponse {

    public JwtAuthenticationResponse(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String tokenType = "Bearer";
}
