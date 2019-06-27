package nikita.webapp.spring.security.configs.authentication.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

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
 * curl -v -H 'Authorization: Basic bmlraXRhLWNsaWVudDpzZWNyZXQ='
 * -X POST  'http://127.0.1.1:8092/noark5v5/oauth/token?grant_type=password&client_id=nikita-client&username=admin&password=password'
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
@Profile("security-oauth2-authentication")
@EnableAuthorizationServer
@Configuration
public class OAuth2AuthorizationServerConfiguration
        extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    @Value("${security.oauth2.client.client-id}")
    private String oauth2ClientId;

    @Value("${security.oauth2.client.client-secret}")
    private String oauth2Secret;

    @Value("${security.oauth2.client.token-expiry:28800}")
    private Integer tokenExpiry;

    public OAuth2AuthorizationServerConfiguration(
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
                .accessTokenValiditySeconds(tokenExpiry)
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
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(
            AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.checkTokenAccess("permitAll()");
    }

    /**
     * Create the tokenStore bean.
     *
     * Currently we are using an InMemoryTokenStore. In production this should
     * be swapped out with a database tokenstore, but given the use-case
     * (low-volume, not many restarts) for this application, it might be
     * acceptable to use an InMemory. The consequence is that if the
     * application restarts, no existing tokens will be valid and users will
     * have to re-authenticate.
     *
     * @return the newly created token store
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}

