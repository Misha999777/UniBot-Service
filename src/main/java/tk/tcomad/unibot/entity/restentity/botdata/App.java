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
public class App extends AbstractRestEntity implements BotData {

    @Override
    public String getInfo() {
        return url;
    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    private Bot bot = super.botFromAuth;
}