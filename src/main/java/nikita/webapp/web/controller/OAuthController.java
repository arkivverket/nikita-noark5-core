package nikita.webapp.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.LOGOUT_PATH;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

/**
 *
 * The code is initially adapted from:
 *
 * https://stackoverflow.com/questions/21987589/spring-security-how-to-log-out-user-revoke-oauth2-token#32320860
 *
 */
@Controller
public class OAuthController {

    private static final Logger logger =
            LoggerFactory.getLogger(OAuthController.class);

    private TokenStore tokenStore;

    public OAuthController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @PostMapping(value = LOGOUT_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            logger.info("Removed the following token " + tokenValue);
            return status(OK).
                    body("{\"status\" : \"Success\"}");
        }
        return status(NOT_FOUND).
                body("{\"status\" : \"Du var ikke pålogget\"}");
    }
}