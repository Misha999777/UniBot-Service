package tk.tcomad.unibot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import tk.tcomad.unibot.entity.restentity.Week;
import tk.tcomad.unibot.entity.restentity.botdata.App;
import tk.tcomad.unibot.entity.restentity.botdata.Book;
import tk.tcomad.unibot.entity.restentity.botdata.Lecture;
import tk.tcomad.unibot.entity.restentity.botdata.Lesson;
import tk.tcomad.unibot.entity.restentity.botdata.Student;
import tk.tcomad.unibot.entity.restentity.botdata.Teacher;

@Configuration
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
    restConfig.exposeIdsFor(Week.class);
  }
}
