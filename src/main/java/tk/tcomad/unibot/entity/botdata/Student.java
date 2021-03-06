package tk.tcomad.unibot.entity.botdata;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import tk.tcomad.unibot.entity.Bot;

@Entity
public class Student implements BotData {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String data;
    @Getter
    @Setter
    @ManyToOne
    private Bot bot;

    public String getInfo() {
        return data;
    }

    @PrePersist
    public void prePersist() {
        this.bot.getStudents().add(this);
    }

    @PreRemove
    public void preRemove() {
        this.bot.getStudents().remove(this);
    }
}
