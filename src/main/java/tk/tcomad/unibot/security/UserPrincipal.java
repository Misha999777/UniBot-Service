package tk.tcomad.unibot.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tk.tcomad.unibot.entity.Bot;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final Bot bot;

    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(Bot bot) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new UserPrincipal(
                bot,
                authorities
        );
    }

    public Bot getBot() {
        return bot;
    }

    @Override
    public String getUsername() {
        return bot.getUsername();
    }

    @Override
    public String getPassword() {
        return bot.getApi();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
