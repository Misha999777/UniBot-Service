package tk.tcomad.unibot.entity.restentity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import tk.tcomad.unibot.entity.Bot;

@Entity
public class Week extends AbstractRestEntity {

    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String date;

    @Getter
    @Setter
    @ManyToOne
    private Bot bot = super.botFromAuth;
}
