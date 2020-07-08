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
public class Student extends AbstractRestEntity implements BotData  {

    public String getInfo() {
        return data;
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
    private String data;

    @Getter
    @Setter
    @ManyToOne
    private Bot bot = super.botFromAuth;
}
