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
import tk.tcomad.unibot.entity.restentity.botdata.Book;

@CrossOrigin
@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAllByBot(Bot bot);
    Optional<Book> findBookById(Long id);

    @Override
    @NonNull
    @RestResource
    <S extends Book> S save(@NonNull S book);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.api == principal.password")
    Optional<Book> findById(@NonNull Long id);

    @Override
    @NonNull
    @RestResource
    @PostFilter("filterObject.bot.api == principal.password")
    Iterable<Book> findAll();

    @Override
    @RestResource
    @PreAuthorize("#book.bot.api==principal.password")
    void delete(@NonNull Book book);
}
