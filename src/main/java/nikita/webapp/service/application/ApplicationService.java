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

    @Value("${spring.application.name}")
    private String product;

    @Value("${nikita.system.vendor.version}")
    private String productVersion;

    @Value("${nikita.system.vendor.name}")
    private String vendor;

    @Value("${nikita.system.protocol.version}")
    private String protocolVersion;

    @Value("${nikita.system.build}")
    private String versionDate;

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
     * Creates a list of the officially supported resource links.
     * These are: arkivstruktur, casehandling, metadata, admin and
     * loggingogsporing
     *
     */
    public void addConformityLevels(List<ConformityLevel> conformityLevels) {
        // arkivstruktur
        addConformityLevel(conformityLevels, HREF_BASE_FONDS_STRUCTURE + SLASH,
                REL_FONDS_STRUCTURE);
        // sakarkiv
        addConformityLevel(conformityLevels, HREF_BASE_CASE_HANDLING + SLASH,
                REL_CASE_HANDLING);
        // metadata
        addConformityLevel(conformityLevels, HREF_BASE_METADATA + SLASH, REL_METADATA);
        // administrasjon
        addConformityLevel(conformityLevels, HREF_BASE_ADMIN + SLASH,
                REL_ADMINISTRATION);
        // loggingogsporing
        addConformityLevel(conformityLevels, HREF_BASE_LOGGING,
                REL_LOGGING);
    }

    public ApplicationDetails getApplicationDetails() {
        ArrayList<ConformityLevel> conformityLevels = new ArrayList<>();
        ApplicationDetails applicationDetails =
                new ApplicationDetails(conformityLevels);
        applicationDetails.setSelfHref(getOutgoingAddress());

        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();
        // If you are logged in, add more information
        if (!username.equals("anonymousUser")) {
            addConformityLevels(conformityLevels);
        }
        addOpenIdConfiguration(conformityLevels);
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

    public LoggingDetails getLoggingDetails() {
        return new LoggingDetails(getOutgoingAddress());
    }

    public SystemInformation getSystemInformation() {
        SystemInformation systemInformation = new SystemInformation();
        systemInformation.setProduct(product);
        systemInformation.setProtocolVersion(protocolVersion);
        systemInformation.setVendor(vendor);
        systemInformation.setVersion(productVersion);
        systemInformation.setVersionDate(versionDate);
        return systemInformation;
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
        OIDCConfiguration oidcConfiguration = new OIDCConfiguration();
        oidcConfiguration.setIssuer(baseAddress);
        oidcConfiguration.setAuthorizationEndpoint(baseAddress + LOGIN_PATH);
        oidcConfiguration.setRevocationEndpoint(baseAddress + LOGOUT_PATH);
        oidcConfiguration.setRegistrationEndpoint(
                baseAddress + HREF_BASE_ADMIN + SLASH + NEW_USER);
        oidcConfiguration.setIntrospectionEndpoint(
                baseAddress + CHECK_TOKEN_PATH);
        oidcConfiguration.setGrantTypesSupported(Arrays.asList(
                "password", "authorization_code", "refresh_token"));
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

    /**
     * Add a conformity level
     *
     * @param conformityLevels ArrayList of conformityLevels
     * @param href             http address of endpoint
     * @param rel              rel associated with endpoint
     */
    private void addConformityLevel(List<ConformityLevel> conformityLevels,
                                    String href, String rel) {
        ConformityLevel conformityLevel = new ConformityLevel();
        conformityLevel.setHref(getOutgoingAddress() + href);
        conformityLevel.setRel(rel);
        conformityLevels.add(conformityLevel);
    }
}
