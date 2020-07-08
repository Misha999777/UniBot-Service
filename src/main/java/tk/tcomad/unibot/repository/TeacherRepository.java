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
import tk.tcomad.unibot.entity.restentity.botdata.Teacher;

@CrossOrigin
@RepositoryRestResource
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    List<Teacher> findAllByBot(Bot bot);
    Optional<Teacher> findTeacherById(Long id);

    @Override
    @NonNull
    @RestResource
    <S extends Teacher> S save(@NonNull S teacher);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.api == principal.password")
    Optional<Teacher> findById(@NonNull Long id);

    @Override
    @NonNull
    @RestResource
    @PostFilter("filterObject.bot.api == principal.password")
    Iterable<Teacher> findAll();

    @Override
    @RestResource
    @PreAuthorize("#teacher.bot.api==principal.password")
    void delete(@NonNull Teacher teacher);
}
