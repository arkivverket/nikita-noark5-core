package nikita.webapp.spring;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.ManagedBean;
import java.util.Optional;

@ManagedBean
public class AuditorAwareImpl
        implements AuditorAware<String> {

    /**
     * Note this can handle entities created during startup (system) or the
     * actual logged in user.
     *
     * @return
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        }
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("system");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Optional.of(userDetails.getUsername());
    }
}
