package nikita.webapp.spring.security.configs.authentication.form;

import nikita.webapp.spring.security.NikitaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import static nikita.common.config.Constants.ROLE_RECORDS_MANAGER;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static nikita.common.config.PATHPatterns.PATTERN_METADATA_PATH;
import static nikita.common.config.PATHPatterns.PATTERN_NEW_FONDS_STRUCTURE_ALL;

/**
 * Testing a form based SecurityConfig using the pre-provided users/authorities
 * defined in a sql file in the resources directory
 * <p>
 * This can be tested using:
 * <p>
 * curl -i -X POST -d username=admin -d password=password -c /tmp/cookies.txt
 * http://localhost:8092/noark5v5/login
 * <p>
 * <p>
 * curl -i  -b /tmp/cookies.txt --header Accept:application/vnd.noark5+json
 * -X GET http://localhost:8092/noark5v5/
 */

@Profile("security-form-authentication")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true,
        jsr250Enabled = true)
public class FormAuthenticationConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private NikitaUserDetailsService userDetailsService;

    @Autowired
    private NikitaAuthenticationSuccessHandler authenticationSuccessHandler;

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
     *
     * @return new BCryptPasswordEncoder()
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception { // @formatter:off

        httpSecurity
                .cors().and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                // GET [api]/metadata/**, public to read basic structure
                .antMatchers(HttpMethod.GET, PATTERN_METADATA_PATH).permitAll()
                // POST GET [api]/arkivstruktur/ny-*, need role of record keeper
                .antMatchers(HttpMethod.POST, PATTERN_NEW_FONDS_STRUCTURE_ALL).hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.GET, PATTERN_NEW_FONDS_STRUCTURE_ALL).hasAuthority(ROLE_RECORDS_MANAGER)
                // POST PUT PATCH [api]/arkivstruktur/**, need role of record keeper
                .antMatchers(HttpMethod.PUT, FONDS + SLASH + "**").hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.PATCH, FONDS + SLASH + "**").hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.DELETE, "/**").hasAuthority(ROLE_RECORDS_MANAGER)
                // POST PUT PATCH DELETE [api]/metadata/**, need role of record keeper
                .antMatchers(HttpMethod.PATCH, PATTERN_METADATA_PATH).hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.PUT, PATTERN_METADATA_PATH).hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.POST, PATTERN_METADATA_PATH).hasAuthority(ROLE_RECORDS_MANAGER)
                .antMatchers(HttpMethod.DELETE, PATTERN_METADATA_PATH).hasAuthority(ROLE_RECORDS_MANAGER)
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
        ;
    } // @formatter:on
}
