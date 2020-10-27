package tk.tcomad.unibot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.BotUser;

@Repository
public interface UserRepository extends CrudRepository<BotUser, Long> {

    List<BotUser> findAllByBot(Bot bot);

    Boolean existsByChatIDAndBot(Long chat, Bot bot);
}
