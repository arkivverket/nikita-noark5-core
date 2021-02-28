package nikita.webapp.spring;

import nikita.webapp.spring.security.NikitaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * This is the ResourceServerConfiguration for the application. It sets up the
 * AuthenticationProvider and configures the security the the API-endpoints.
 * <p>
 * The security of the API-endpoints are defined in the
 * configure(HttpSecurity http) method.
 */
@EnableResourceServer
@EnableWebSecurity
@Configuration
public class TestResourceServerConfiguration
        extends ResourceServerConfigurerAdapter {

    private NikitaUserDetailsService userDetailsService;

    public TestResourceServerConfiguration(
            NikitaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Create the AuthenticationProvider.
     * <p>
     * DaoAuthenticationProvider used to be able to retrieve details from a
     * UserDetailsService object.
     * <p>
     * Here we set the userDetailsService and password encoder for the
     * userDetailsService
     *
     * @return the newly created AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    /**
     * Create the password encoder.
     * <p>
     * We are using a BCrypt Password Encoder. This is standard for Spring5
     * from what I can tell.
     *
     * @return new BCryptPasswordEncoder()
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Setup the security for the application.
     * <p>
     * The following is a description of the security profile for the
     * application:
     * - All requests to the application must be authenticated
     * - Stateless session
     * - Disable csrf as the token is deemed safe
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
}
