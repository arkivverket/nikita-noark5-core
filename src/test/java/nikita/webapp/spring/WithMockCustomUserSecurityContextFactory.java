package nikita.webapp.spring;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        CustomUserDetails principal = new CustomUserDetails();
        securityContext.setAuthentication(new
                AnonymousAuthenticationToken("admin@example.com", principal,
                Arrays.asList(
                        new SimpleGrantedAuthority("RECORDS_MANAGER"))));
        return securityContext;
    }
}
