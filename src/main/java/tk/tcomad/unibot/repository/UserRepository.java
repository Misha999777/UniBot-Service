package tk.tcomad.unibot.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  List<User> findAllByBot(Bot bot);
  Boolean existsByChatIDAndBot(Long chat, Bot bot);
}