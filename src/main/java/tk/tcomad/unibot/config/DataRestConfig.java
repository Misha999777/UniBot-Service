package tk.tcomad.unibot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.botdata.*;

@Configuration
@AllArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfig) {
        restConfig.disableDefaultExposure();
        restConfig.exposeIdsFor(App.class);
        restConfig.exposeIdsFor(Book.class);
        restConfig.exposeIdsFor(Lecture.class);
        restConfig.exposeIdsFor(Lesson.class);
        restConfig.exposeIdsFor(Student.class);
        restConfig.exposeIdsFor(Teacher.class);
        restConfig.exposeIdsFor(Bot.class);
    }
}
