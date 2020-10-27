package tk.tcomad.unibot.config;

import com.amazonaws.util.EC2MetadataUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@Configuration
public class TelegramConfig {

    @Getter
    @Value("${bot.hourToSendTimetable:#{7}}")
    private int hour;
    @Getter
    @Value("${bot.creatorId:#{397052865}}")
    private int creatorId;
    @Value("${bot.instanceId:#{1}}")
    private String instanceId;

    public String getInstanceId() {
        return StringUtils.defaultIfEmpty(EC2MetadataUtils.getInstanceId(), instanceId);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        ApiContextInitializer.init();
        return new TelegramBotsApi();
    }

}
