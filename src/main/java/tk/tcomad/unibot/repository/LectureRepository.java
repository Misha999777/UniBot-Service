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
import tk.tcomad.unibot.entity.botdata.Lecture;

@CrossOrigin
@RepositoryRestResource
public interface LectureRepository extends CrudRepository<Lecture, Long> {

    List<Lecture> findAllByBot(Bot bot);

    Optional<Lecture> findLectureById(Long id);

    @Override
    @NonNull
    @RestResource
    @PreAuthorize("#lecture.bot.user == authentication.name")
    <S extends Lecture> S save(@NonNull S lecture);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.user == authentication.name")
    Optional<Lecture> findById(@NonNull Long id);

    @Override
    @RestResource
    @PreAuthorize("#lecture.bot.user == authentication.name")
    void delete(@NonNull Lecture lecture);
}
