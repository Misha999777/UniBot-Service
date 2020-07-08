package tk.tcomad.unibot.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Bot implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
}
