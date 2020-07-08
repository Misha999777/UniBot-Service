package tk.tcomad.unibot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tk.tcomad.unibot.entity.Bot;

@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
  Optional<Bot> findByUsername(String username);
  Boolean existsByApi(String api);
  List<Bot> findAllByInstanceId(String instanceId);
}
