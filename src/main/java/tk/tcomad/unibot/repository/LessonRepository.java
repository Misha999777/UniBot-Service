package tk.tcomad.unibot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.botdata.Lesson;

@CrossOrigin
@RepositoryRestResource
public interface LessonRepository extends CrudRepository<Lesson, Long> {

    List<Lesson> findAllByBotAndDay(Bot bot, Integer day);

    Optional<Lesson> findLessonById(Long id);

    @Override
    @NonNull
    @RestResource
    @PreAuthorize("#lesson.bot.user == authentication.name")
    <S extends Lesson> S save(@NonNull S lesson);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.user == authentication.name")
    Optional<Lesson> findById(@NonNull Long id);

    @Override
    @RestResource
    @PreAuthorize("#lesson.bot.user == authentication.name")
    void delete(@NonNull Lesson lesson);
}
