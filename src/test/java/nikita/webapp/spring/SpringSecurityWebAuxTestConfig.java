package nikita.webapp.spring;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
@Import(TestSecurityConfiguration.class)
public class SpringSecurityWebAuxTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User user = new User("admin@example.com", "password",
                Arrays.asList(
                        new SimpleGrantedAuthority("RECORDS_MANAGER")));
        return new InMemoryUserDetailsManager(Arrays.asList(user));
    }
}
