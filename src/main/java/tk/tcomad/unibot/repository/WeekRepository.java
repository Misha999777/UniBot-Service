package tk.tcomad.unibot.repository;

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
import tk.tcomad.unibot.entity.restentity.Week;

@CrossOrigin
@RepositoryRestResource
public interface WeekRepository extends CrudRepository<Week, Long> {
    Optional<Week> findByBot(Bot bot);

    @Override
    @NonNull
    @RestResource
    <S extends Week> S save(@NonNull S week);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.api == principal.password")
    Optional<Week> findById(@NonNull Long id);

    @Override
    @NonNull
    @RestResource
    @PostFilter("filterObject.bot.api == principal.password")
    Iterable<Week> findAll();

    @Override
    @RestResource
    @PreAuthorize("#week.bot.api==principal.password")
    void delete(@NonNull Week week);
}
