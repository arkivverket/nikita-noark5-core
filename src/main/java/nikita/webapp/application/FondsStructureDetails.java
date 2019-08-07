package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@JsonSerialize(using = APIDetailsSerializer.class)
public class FondsStructureDetails extends APIDetails {

    public FondsStructureDetails(String publicUrlPath) {
        super();

        if(publicUrlPath.endsWith("/") == true) {
            publicUrlPath = publicUrlPath.substring(0, publicUrlPath.length()
                    -1);
        }
        // Add Fonds
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS,
                REL_FONDS_STRUCTURE + FONDS + SLASH,
                true
        ));

        // Add new-fonds
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + NEW_FONDS,
                REL_FONDS_STRUCTURE + NEW_FONDS + SLASH,
                false
        ));

        // Add FondsCreator
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS_CREATOR,
                REL_FONDS_STRUCTURE + FONDS_CREATOR + SLASH,
                true
        ));


        // Add new FondsCreator
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + NEW_FONDS_CREATOR,
                REL_FONDS_STRUCTURE + NEW_FONDS_CREATOR + SLASH,
                true
        ));

        // Add Series
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + SERIES,
                REL_FONDS_STRUCTURE + SERIES + SLASH,
                true
        ));

        // Add Classification_system
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH +
                        CLASSIFICATION_SYSTEM,
                REL_FONDS_STRUCTURE + CLASSIFICATION_SYSTEM + SLASH,
                true
        ));

        // Add Class
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASS,
                REL_FONDS_STRUCTURE + CLASS + SLASH,
                true
        ));

        // Add File
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + FILE,
                REL_FONDS_STRUCTURE + FILE + SLASH,
                true
        ));

        // Add Registration
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + RECORD,
                REL_FONDS_STRUCTURE + RECORD + SLASH,
                true
        ));

        // Add DocumentDescription
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH +
                        DOCUMENT_DESCRIPTION,
                REL_FONDS_STRUCTURE + DOCUMENT_DESCRIPTION + SLASH,
                true
        ));

        // Add DocumentObject
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + DOCUMENT_OBJECT,
                REL_FONDS_STRUCTURE + DOCUMENT_OBJECT + SLASH,
                true
        ));
    }
}
