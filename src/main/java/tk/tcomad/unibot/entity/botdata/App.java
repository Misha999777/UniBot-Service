package tk.tcomad.unibot.entity.botdata;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import tk.tcomad.unibot.entity.Bot;

@Entity
public class App implements BotData {

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
    private String url;
    @Getter
    @Setter
    @ManyToOne
    private Bot bot;

    @Override
    public String getInfo() {
        return url;
    }

    @PrePersist
    public void prePersist() {
        this.bot.getApps().add(this);
    }

    @PreRemove
    public void preRemove() {
        this.bot.getApps().remove(this);
    }
}
