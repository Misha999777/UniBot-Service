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
import tk.tcomad.unibot.entity.botdata.Student;

@CrossOrigin
@RepositoryRestResource
public interface StudentRepository extends CrudRepository<Student, Long> {

    List<Student> findAllByBot(Bot bot);

    Optional<Student> findStudentById(Long id);

    @Override
    @NonNull
    @RestResource
    @PreAuthorize("#student.bot.user == authentication.name")
    <S extends Student> S save(@NonNull S student);

    @Override
    @NonNull
    @RestResource
    @PostAuthorize("returnObject.isEmpty() || returnObject.get().bot.user == authentication.name")
    Optional<Student> findById(@NonNull Long id);

    @Override
    @RestResource
    @PreAuthorize("#student.bot.user == authentication.name")
    void delete(@NonNull Student student);
}
