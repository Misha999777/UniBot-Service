package tk.tcomad.unibot.entity.restentity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.security.UserPrincipal;

public abstract class AbstractRestEntity {

  protected Bot botFromAuth;

  public AbstractRestEntity() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      this.botFromAuth = ((UserPrincipal) authentication.getPrincipal()).getBot();
    }
  }
}
