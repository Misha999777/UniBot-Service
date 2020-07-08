package tk.tcomad.unibot.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.repository.BotRepository;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    BotRepository botRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Bot bot = botRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + username)
                );
        return UserPrincipal.create(bot);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Bot bot = botRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(bot);
    }
}
