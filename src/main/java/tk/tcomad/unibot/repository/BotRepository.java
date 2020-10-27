package tk.tcomad.unibot.repository;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import tk.tcomad.unibot.entity.Bot;

@CrossOrigin
@RepositoryRestResource
public interface BotRepository extends CrudRepository<Bot, Long> {

    Boolean existsByApi(String api);

    List<Bot> findAllByInstanceId(String instanceId);

    Bot findByApi(String api);

    @NotNull
    @Override
    @RestResource
    @PostFilter("filterObject.user == authentication.name")
    Iterable<Bot> findAll();

    @NotNull
    @Override
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().user == authentication.name")
    Optional<Bot> findById(@NotNull Long id);
}
