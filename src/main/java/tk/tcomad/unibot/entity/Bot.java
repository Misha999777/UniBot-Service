package tk.tcomad.unibot.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;
import tk.tcomad.unibot.entity.botdata.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Bot implements Serializable {

    public Bot(@NonNull String api, @NonNull String username, @NonNull String instanceId) {
        this.api = api;
        this.username = username;
        this.instanceId = instanceId;
    }

    @Getter
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    @NonNull
    private String api;
    @Getter
    @Setter
    @NonNull
    private String username;
    @Getter
    @Setter
    @NonNull
    private String instanceId;
    @Getter
    @Setter
    private String user;
    @Getter
    @Setter
    private String educationAppGroupId;
    @Getter
    @Setter
    private String weekStart;
    @Getter
    @Setter
    @OneToMany
    private List<App> apps;
    @Getter
    @Setter
    @OneToMany
    private List<Book> books;
    @Getter
    @Setter
    @OneToMany
    private List<Lecture> lectures;
    @Getter
    @Setter
    @OneToMany
    private List<Lesson> lessons;
    @Getter
    @Setter
    @OneToMany
    private List<Student> students;
    @Getter
    @Setter
    @OneToMany
    private List<Teacher> teachers;
}
