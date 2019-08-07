package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@JsonSerialize(using = APIDetailsSerializer.class)
public class AdministrationDetails extends APIDetails {

    public AdministrationDetails(String publicUrlPath) {
        super();
        // Add support for AdministrativeUnit
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + ADMINISTRATIVE_UNIT,
                REL_ADMINISTRATION + ADMINISTRATIVE_UNIT + SLASH,
                true
        ));

        // Add support for new AdministrativeUnit
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + NEW_ADMINISTRATIVE_UNIT,
                REL_ADMINISTRATION + NEW_ADMINISTRATIVE_UNIT + SLASH,
                true
        ));

        // Add support for User
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + USER,
                REL_ADMINISTRATION + USER + SLASH,
                true
        ));

        // Add support for new User
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + NEW_USER,
                REL_ADMINISTRATION + NEW_USER + SLASH,
                true
        ));

        // Add support for Right
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + RIGHT,
                REL_ADMINISTRATION + RIGHT + SLASH,
                true
        ));

        // Add support for new Right
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_ADMINISTRATION_PATH + SLASH + NEW_RIGHT,
                REL_ADMINISTRATION + NEW_RIGHT + SLASH,
                true
        ));
    }
}
