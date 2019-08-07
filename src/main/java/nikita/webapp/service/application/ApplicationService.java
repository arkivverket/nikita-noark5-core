package nikita.webapp.service.application;

import nikita.webapp.application.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.NEW_USER;

@Service
@Transactional
public class ApplicationService {

    @Value("${nikita.server.hateoas.publicAddress}")
    private String publicAddress;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Add OIDC as an option
     * https://rel.arkivverket.no/noark5/v5/api/login/oidc/
     */
    public void addOpenIdConfiguration(List<ConformityLevel> conformityLevels) {
        ConformityLevel openId = new ConformityLevel();
        openId.setHref(getOutgoingAddress() + HREF_OPENID_CONFIGURATION);
        openId.setRel(REL_LOGIN_OIDC);
        conformityLevels.add(openId);
    }

    /**
     * Adds check token mechanism to hateoas links
     */
    public void addCheckToken(List<ConformityLevel> conformityLevels) {
        ConformityLevel checkTokenOauth2 = new ConformityLevel();

        checkTokenOauth2.setHref(getOutgoingAddress() + CHECK_TOKEN_PATH);
        checkTokenOauth2.setRel(NIKITA_CONFORMANCE_REL + CHECK_TOKEN_PATH
                + SLASH + LOGIN_OAUTH + SLASH);
        conformityLevels.add(checkTokenOauth2);
    }

    /**
     * Creates a list of the officially supported resource links.
     * These are: arkivstruktur, casehandling, metadata, admin and
     * loggingogsporing
     *
     */
    public void addConformityLevels(List<ConformityLevel> conformityLevels) {
        // ConformityLevel : arkivstruktur
        ConformityLevel conformityLevelFondsStructure = new ConformityLevel();
        conformityLevelFondsStructure.setHref(
                getOutgoingAddress() + HREF_BASE_FONDS_STRUCTURE);
        conformityLevelFondsStructure.setRel(REL_FONDS_STRUCTURE);
        conformityLevels.add(conformityLevelFondsStructure);

        // ConformityLevel : casehandling
        ConformityLevel conformityLevelCaseHandling = new ConformityLevel();
        conformityLevelCaseHandling.setHref(
                getOutgoingAddress() + HREF_BASE_CASE_HANDLING);
        conformityLevelCaseHandling.setRel(REL_CASE_HANDLING);
        conformityLevels.add(conformityLevelCaseHandling);

        // ConformityLevel : metadata
        ConformityLevel conformityLevelMetadata = new ConformityLevel();
        conformityLevelMetadata.setHref(
                getOutgoingAddress() + HREF_BASE_METADATA);
        conformityLevelMetadata.setRel(REL_METADATA);
        conformityLevels.add(conformityLevelMetadata);

        // ConformityLevel : administrasjon
        ConformityLevel conformityLevelAdministration = new ConformityLevel();
        conformityLevelAdministration.setHref(getOutgoingAddress() +
                HREF_BASE_ADMIN);
        conformityLevelAdministration.setRel(REL_ADMINISTRATION);
        conformityLevels.add(conformityLevelAdministration);

        /*
        // Will be introduced as project mature
        // ConformityLevel : loggingogsporing
        ConformityLevel conformityLevelLogging = new ConformityLevel();
        conformityLevelLogging.setHref(getOutgoingAddress() +
        HREF_BASE_LOGGING);
        conformityLevelLogging.setRel(REL_LOGGING);
        conformityLevels.add(conformityLevelLogging);
        */
    }

    public ApplicationDetails getApplicationDetails() {
        ApplicationDetails applicationDetails;
        ArrayList<ConformityLevel> conformityLevels = new ArrayList<>();
        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();
        // If you are logged in, add more information
        if (!username.equals("anonymousUser")) {
            addConformityLevels(conformityLevels);
            addCheckToken(conformityLevels);
        }
        addOpenIdConfiguration(conformityLevels);
        applicationDetails = new ApplicationDetails(conformityLevels);
        return applicationDetails;
    }

    public FondsStructureDetails getFondsStructureDetails() {
        return new FondsStructureDetails(getOutgoingAddress());
    }

    public AdministrationDetails getAdministrationDetails() {
        return new AdministrationDetails(getOutgoingAddress());
    }

    public MetadataDetails getMetadataDetails() {
        return new MetadataDetails(getOutgoingAddress());
    }

    public CaseHandlingDetails getCaseHandlingDetails() {
        return new CaseHandlingDetails(getOutgoingAddress());
    }

    /**
     * Create a minimal OIDCConfiguration that can be used to login to the
     * server. It is a little unclear to us at the moment how this should be
     * populated. Very little is said in the standard about this, and we have
     * to make things up as we go along. The client_id to log onto the server
     * is not exposed here so it will have to be manually configured in
     * clients when logging on. Or we can switch off Basic Auth for the OAuth2
     * endpoints.
     *
     * @return A minimal OIDCConfiguration that can be used to login to the
     * server
     */
    public OIDCConfiguration getOpenIdConfiguration() {
        String baseAddress = getOutgoingAddress();

        String authorisationEndpoint = baseAddress + LOGIN_PATH;
        String revocationEndpoint = baseAddress + LOGOUT_PATH;
        String registrationEndpoint = baseAddress + HREF_BASE_ADMIN + NEW_USER;
        ArrayList <String> grantTypes = new ArrayList<>(Arrays.asList(
                "password", "authorization_code", "refresh_token"));

        OIDCConfiguration oidcConfiguration = new OIDCConfiguration();
        oidcConfiguration.setIssuer(baseAddress);
        oidcConfiguration.setAuthorizationEndpoint(authorisationEndpoint);
        oidcConfiguration.setRevocationEndpoint(revocationEndpoint);
        oidcConfiguration.setRegistrationEndpoint(registrationEndpoint);
        oidcConfiguration.setGrantTypesSupported(grantTypes);
        oidcConfiguration.setTokenEndpointAuthMethodsSupported(
                Arrays.asList("client_secret_basic"));
        oidcConfiguration.setRequestParameterSupported(true);

        return oidcConfiguration;
    }

    /**
     * Get the outgoing address to use when generating links.
     * If we are not running behind a front facing server incoming requests
     * will not have X-Forward-* values set. In this case use the hardcoded
     * value from the properties file.
     * <p>
     * If X-Forward-*  values are set, then use them. At a minimum Host and
     * Proto must be set. If Port is also set use this to.
     *
     * @return the outgoing address
     */
    protected String getOutgoingAddress() {
        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");
        String port = request.getHeader("X-Forwarded-Port");

        if (address != null && protocol != null) {
            if (port != null) {
                return protocol + "://" + address + ":" + port + contextPath +
                        SLASH;
            } else {
                return protocol + "://" + address + contextPath + SLASH;
            }
        } else {
            return publicAddress + contextPath + SLASH;
        }
    }
}
