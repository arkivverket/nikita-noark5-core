package nikita.webapp.spring.security.configs.authentication.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * This is the AuthorizationServer for the application. It sets up the
 * required beans in order to provide authorization. Grouse includes its own
 * AuthorizationServer that stores usernames and hashed passwords (BCrypt)
 * in the database. Later we envision that we could swap this out and have
 * login via a third party (gmail etc), that's why we have implemented Oauth2.
 *
 *
 * Typically the oauth endpoint to grab an access token is:
 *     oauth/token
 *
 * You can test that the authorization server is working by attempting to
 * login to nikita with oauth2 support via curl:
 * <p>
 * curl -v -X POST  -H 'Authorization: Basic bmlraXRhLWNsaWVudDpzZWNyZXQ='
 * http://127.0.1.1:8092/noark5v5/oauth/token -d grant_type=password -d
 * username=admin -d password=password
 * <p>
 * bmlraXRhLWNsaWVudDpzZWNyZXQ= is 'nikita-client:secret' base64 encoded.
 * You will need to make sure this matches with the *.yml files where the
 * following are defined:
 *
 * @Value("${security.oauth2.client.client-id}")
 * @Value("${security.oauth2.client.client-secret}")
 * <p>

 *
 *
 * Obviously everything should be on one line.
 *
 * Note. The passwords in the database only work if {bcrypt} is not present
 * in the password column. This is perplexing as I believe that
 * spring-security5 actually requires the password field to start
 * like: "{bcrypt}REST OF HASHED PASSWORD". Stepping through the code shows
 * that {bcrypt} should not be there. Leaving this comment here in case this
 * becomes an issue later.
 * 
 *
 */
@Profile("security-oauth2-jwt-authentication")
@EnableAuthorizationServer
@Configuration
public class OAuth2JwtAuthorizationServerConfiguration
        extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    @Value("${security.oauth2.client.client-id}")
    private String oauth2ClientId;

    @Value("${security.oauth2.client.client-secret}")
    private String oauth2Secret;

    @Value("${nikita.security.jwt.signkey:nikitasignkey}")
    private String jwtSignKey;

    public OAuth2JwtAuthorizationServerConfiguration(
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSignKey);
        return converter;
    }
    /**
     * Create the tokenStore bean.
     *
     * @return the newly created token store
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    /**
     * Configure the ClientDetailsService. This declares the
     * properties of the oauth2 client and its properties.
     *
     * This requires the existence of an AuthenticationManager bean. This bean
     * is defined in WebSecurityConfig. I was unable to define the bean
     * elsewhere.
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.inMemory()
                .withClient(oauth2ClientId)
                .authorizedGrantTypes(
                        "password", "authorization_code", "refresh_token")
                .secret(oauth2Secret)
                .scopes("all");
    }

    /**
     * Configure the endpoints to use tokenStore and the
     * authenticationManager
     */
    @Override
    public void configure(
            AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter());
    }
}

