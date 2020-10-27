package tk.tcomad.unibot.entity.botdata;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import tk.tcomad.unibot.entity.Bot;

@Entity
public class Lesson implements BotData {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Bot bot;

    public String getName() {
        return lessonOfFirstWeek;
    }

    public String getInfo() {
        return lessonOfSecondWeek;
    }

    public String getWeekLesson(boolean isEvenWeek) {
        return isEvenWeek
                ? lessonOfFirstWeek
                : lessonOfSecondWeek;
    }

    @PrePersist
    public void prePersist() {
        this.bot.getLessons().add(this);
    }

    @PreRemove
    public void preRemove() {
        this.bot.getLessons().remove(this);
    }
}
