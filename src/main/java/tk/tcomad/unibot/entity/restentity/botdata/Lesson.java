package tk.tcomad.unibot.entity.restentity.botdata;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.restentity.AbstractRestEntity;

@Entity
public class Lesson extends AbstractRestEntity implements BotData  {

    public String getName() {
        return lessonOfFirstWeek;
    }

    public String getInfo() {
        return lessonOfSecondWeek;
    }

    public String getWeekLesson(boolean isEvenWeek) {
        return isEvenWeek ? lessonOfFirstWeek : lessonOfSecondWeek;
    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Integer day;

    @Getter
    @Setter
    private String lessonOfFirstWeek;

    @Getter
    @Setter
    private String lessonOfSecondWeek;

    @Getter
    @Setter
    @ManyToOne
    private Bot bot = super.botFromAuth;
}
