package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;

@JsonSerialize(using = APIDetailsSerializer.class)
public class AdministrationDetails extends APIDetails {

    public AdministrationDetails(String publicUrlPath) {
        super();

        // Add support for system information
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_SYSTEM_INFORMATION,
                REL_SYSTEM_INFORMATION, false));

        // Add support for AdministrativeUnit
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + ADMINISTRATIVE_UNIT,
                REL_ADMINISTRATION + ADMINISTRATIVE_UNIT + SLASH,
                true
        ));

        // Add support for new AdministrativeUnit
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + NEW_ADMINISTRATIVE_UNIT,
                REL_ADMINISTRATION + NEW_ADMINISTRATIVE_UNIT + SLASH,
                false
        ));

        // Add support for User
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + USER,
                REL_ADMINISTRATION + USER + SLASH,
                true
        ));

        // Add support for new User
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + NEW_USER,
                REL_ADMINISTRATION + NEW_USER + SLASH,
                false
        ));

        // Add support for Right
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + RIGHT,
                REL_ADMINISTRATION + RIGHT + SLASH,
                true
        ));

        // Add support for new Right
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN + NEW_RIGHT,
                REL_ADMINISTRATION + NEW_RIGHT + SLASH,
                false
        ));

        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_ADMIN,
                SELF, false));

    }
}
