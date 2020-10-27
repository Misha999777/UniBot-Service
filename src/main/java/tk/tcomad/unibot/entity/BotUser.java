package tk.tcomad.unibot.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BotUser {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NonNull
    private Long chatID;

    @Getter
    @Setter
    @NonNull
    @ManyToOne
    private Bot bot;

}

