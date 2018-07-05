package nikita.webapp.spring.security.configs.authentication;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static nikita.common.config.Constants.ROLE_RECORDS_MANAGER;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static nikita.common.config.PATHPatterns.PATTERN_METADATA_PATH;
import static nikita.common.config.PATHPatterns.PATTERN_NEW_FONDS_STRUCTURE_ALL;

@Component
@Profile({"security-oauth2-jwt-authentication",
           "security-oauth2-authentication",
             "security-http-basic-authentication"})
public class NikitaWebSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowedMethods(Arrays.asList("OPTIONS", "POST", "GET", "PUT", "PATCH", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * Setup the security for the application.
     * <p>
     * The following is a description of the security profile for the
     * application:
     * - All requests to the application must be authenticated
     * - Stateless session
     * - Disable csrf as the token is deemed safe
     * <p>
     * This is applicable to the second security layer or (Bearer auth)
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception { // @formatter:off
        httpSecurity
                .authorizeRequests()
                // GET [api]/metadata/**, public to read basic structure
                    .antMatchers(HttpMethod.GET, PATTERN_METADATA_PATH)
                        .permitAll()
                // POST GET [api]/arkivstruktur/ny-*, need role of record keeper
                    .antMatchers(HttpMethod.POST, PATTERN_NEW_FONDS_STRUCTURE_ALL)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.GET, PATTERN_NEW_FONDS_STRUCTURE_ALL)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    // POST PUT PATCH [api]/arkivstruktur/**, need role of record keeper
                    .antMatchers(HttpMethod.PUT, FONDS + SLASH + "**")
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.PATCH, FONDS + SLASH + "**")
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.DELETE, "/**")
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    // POST PUT PATCH DELETE [api]/metadata/**, need role of record keeper
                    .antMatchers(HttpMethod.PATCH, PATTERN_METADATA_PATH)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.PUT, PATTERN_METADATA_PATH)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.POST, PATTERN_METADATA_PATH)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .antMatchers(HttpMethod.DELETE, PATTERN_METADATA_PATH)
                        .hasAuthority(ROLE_RECORDS_MANAGER)
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                .and()
                    .headers()
                        .defaultsDisabled()
                        // disable page caching
                        .cacheControl();
        ;

        httpSecurity.headers().cacheControl();
    } // @formatter:on

}
