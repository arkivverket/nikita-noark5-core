package nikita.webapp.service.application;

import nikita.webapp.application.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.NEW_USER;

@Service
@Transactional
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(
            ApplicationService.class);

    @Value("${hateoas.publicAddress}")
    private String publicUrlPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Creates a list of the supported supported login methods.
     * These are: OAUTH2, JWT via OAUTH2, Basic and Form-based
     * <p>
     * Currently the code only returns OAUTH2. This should detect which
     * profile is running and set links accordingly
     *
     * @return
     */
    public void addLoginInformation(HttpServletRequest request,
                                    List<ConformityLevel> conformityLevels) {
        ConformityLevel loginOauth2 = new ConformityLevel();
        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");

        if (address == null) {
            loginOauth2.setHref(publicUrlPath + LOGIN_PATH);
        } else {
            loginOauth2.setHref(protocol + "://" + address + contextPath +
                    SLASH + LOGIN_PATH);
        }
        loginOauth2.setRel(NIKITA_CONFORMANCE_REL + LOGIN_REL_PATH + SLASH +
                LOGIN_OAUTH + SLASH);
        conformityLevels.add(loginOauth2);
    }

    /**
     * Creates a list of the supported supported logout methods.
     * These are: OAUTH2, JWT via OAUTH2
     * <p>
     * Currently the code only returns OAUTH2. This should detect which
     * profile is running and set links accordingly
     *
     * @return
     */
    public void addLogoutInformation(HttpServletRequest request,
                                     List<ConformityLevel> conformityLevels) {

        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");

        ConformityLevel logoutOauth2 = new ConformityLevel();
        if (address == null) {
            logoutOauth2.setHref(publicUrlPath + LOGOUT_PATH);
        } else {
            logoutOauth2.setHref(protocol + "://" + address + contextPath +
                    SLASH + LOGOUT_PATH);
        }
        logoutOauth2.setRel(NIKITA_CONFORMANCE_REL + LOGOUT_REL_PATH + SLASH +
                LOGIN_OAUTH + SLASH);
        conformityLevels.add(logoutOauth2);
    }

    /**
     * Creates a method to create an account
     *
     * @return
     */
    public void addAccountCreationInformation(HttpServletRequest request,
                                              List<ConformityLevel> conformityLevels) {

        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");

        ConformityLevel accountCreation = new ConformityLevel();
        if (address == null) {
            accountCreation.setHref(publicUrlPath + HATEOAS_API_PATH + SLASH +
                    NOARK_ADMINISTRATION_PATH + SLASH + NEW_USER);
        } else {
            accountCreation.setHref(protocol + "://" + address + contextPath +
                    SLASH + NOARK_ADMINISTRATION_PATH + SLASH + NEW_USER);
        }
        accountCreation.setRel(REL_ADMIN_NEW_USER);
        conformityLevels.add(accountCreation);
    }

    /**
     * Creates a list of the officially supported resource links.
     * These are: arkivstruktur, casehandling, metadata, administrasjon and
     * loggingogsporing
     *
     * @return
     */

    public void addConformityLevels(List<ConformityLevel> conformityLevels) {
        // ConformityLevel : arkivstruktur
        ConformityLevel conformityLevelFondsStructure = new ConformityLevel();
        conformityLevelFondsStructure.setHref(publicUrlPath + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH);
        conformityLevelFondsStructure.setRel(NOARK_CONFORMANCE_REL + NOARK_FONDS_STRUCTURE_PATH + SLASH);
        conformityLevels.add(conformityLevelFondsStructure);

        // ConformityLevel : casehandling
        ConformityLevel conformityLevelCaseHandling = new ConformityLevel();
        conformityLevelCaseHandling.setHref(publicUrlPath + HATEOAS_API_PATH + SLASH + NOARK_CASE_HANDLING_PATH);
        conformityLevelCaseHandling.setRel(NOARK_CONFORMANCE_REL + NOARK_CASE_HANDLING_PATH + SLASH);
        conformityLevels.add(conformityLevelCaseHandling);

        // ConformityLevel : metadata
        ConformityLevel conformityLevelMetadata = new ConformityLevel();
        conformityLevelMetadata.setHref(publicUrlPath + HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH);
        conformityLevelMetadata.setRel(NIKITA_CONFORMANCE_REL + NOARK_METADATA_PATH + SLASH);
        conformityLevels.add(conformityLevelMetadata);


        /*
        // These will be added as the development progresses.
        // They are not really specified properly in the interface standard.
        // ConformityLevel : administrasjon
        ConformityLevel conformityLevelAdministration = new ConformityLevel();
        conformityLevelAdministration.setHref(publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH);
        conformityLevelAdministration.setRel(NOARK_CONFORMANCE_REL + NOARK_ADMINISTRATION_PATH + SLASH);
        conformityLevels.add(conformityLevelAdministration);

        // ConformityLevel : loggingogsporing
        ConformityLevel conformityLevelLogging = new ConformityLevel();
        conformityLevelLogging.setHref(publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_LOGGING_PATH);
        conformityLevelLogging.setRel(NOARK_CONFORMANCE_REL + NOARK_LOGGING_PATH + SLASH);
        conformityLevels.add(conformityLevelLogging);
        */
    }

    public ApplicationDetails getApplicationDetails(HttpServletRequest request) {
        ApplicationDetails applicationDetails;
        ArrayList<ConformityLevel> conformityLevels = new ArrayList(10);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // If you are logged in, add more information
        if (!username.equals("anonymousUser")) {
            addConformityLevels(conformityLevels);
            addLogoutInformation(request, conformityLevels);
        }

        // Show login relation also for logged in users to allow user
        // change also when logged in.
        addLoginInformation(request, conformityLevels);

        // Show accoount creation relation 
        addAccountCreationInformation(request, conformityLevels);

        applicationDetails = new ApplicationDetails(conformityLevels);
        return applicationDetails;
    }

    public FondsStructureDetails getFondsStructureDetails() {
        return new FondsStructureDetails(publicUrlPath);
    }

    public AdministrationDetails getAdministrationDetails() {
        return new AdministrationDetails(publicUrlPath);
    }

    public MetadataDetails getMetadataDetails() {
        return new MetadataDetails(publicUrlPath);
    }

    public CaseHandlingDetails getCaseHandlingDetails() {
        return new CaseHandlingDetails(publicUrlPath);
    }
}
