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
import tk.tcomad.unibot.entity.restentity.botdata.App;

@CrossOrigin
@RepositoryRestResource
public interface AppRepository extends CrudRepository<App, Long> {
    List<App> findAllByBot(Bot bot);
    Optional<App> findAppById(Long id);

    @Override
    @NonNull
    @RestResource
    <S extends App> S save(@NonNull S app);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.api == principal.password")
    Optional<App> findById(@NonNull Long id);

    @Override
    @NonNull
    @RestResource
    @PostFilter("filterObject.bot.api == principal.password")
    Iterable<App> findAll();

    @Override
    @RestResource
    @PreAuthorize("#app.bot.api==principal.password")
    void delete(@NonNull App app);
}
