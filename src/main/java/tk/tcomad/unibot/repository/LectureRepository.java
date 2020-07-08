package tk.tcomad.unibot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.restentity.botdata.Lecture;

@CrossOrigin
@RepositoryRestResource
public interface LectureRepository extends CrudRepository<Lecture, Long> {
    List<Lecture> findAllByBot(Bot bot);
    Optional<Lecture> findLectureById(Long id);

    @Override
    @NonNull
    @RestResource
    <S extends Lecture> S save(@NonNull S lecture);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.api == principal.password")
    Optional<Lecture> findById(@NonNull Long id);

    @Override
    @NonNull
    @RestResource
    @PostFilter("filterObject.bot.api == principal.password")
    Iterable<Lecture> findAll();

    @Override
    @RestResource
    @PreAuthorize("#lecture.bot.api==principal.password")
    void delete(@NonNull Lecture lecture);
}
