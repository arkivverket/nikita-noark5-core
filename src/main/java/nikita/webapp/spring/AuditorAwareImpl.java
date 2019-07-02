package nikita.webapp.spring;

import nikita.common.model.nikita.NikitaUserPrincipal;
import nikita.common.model.noark5.v5.admin.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
        return Optional.of((String)authentication.getPrincipal());
    }
}
