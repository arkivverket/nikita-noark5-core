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
                publicUrlPath + SLASH + HREF_BASE_FONDS,
                REL_FONDS_STRUCTURE + FONDS + SLASH,
                true
        ));

        // Add new-fonds
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + NEW_FONDS,
                REL_FONDS_STRUCTURE + NEW_FONDS + SLASH
        ));

        // Add FondsCreator
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + FONDS_CREATOR,
                REL_FONDS_STRUCTURE + FONDS_CREATOR + SLASH,
                true
        ));


        // Add new FondsCreator
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + NEW_FONDS_CREATOR,
                REL_FONDS_STRUCTURE + NEW_FONDS_CREATOR + SLASH
        ));

        // Add Series
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_SERIES,
                REL_FONDS_STRUCTURE + SERIES + SLASH,
                true
        ));

        // Add Classification_system
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + CLASSIFICATION_SYSTEM,
                REL_FONDS_STRUCTURE + CLASSIFICATION_SYSTEM + SLASH,
                true
        ));

        // Add Class
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_CLASS,
                REL_FONDS_STRUCTURE + CLASS + SLASH,
                true
        ));

        // Add File
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FILE,
                REL_FONDS_STRUCTURE + FILE + SLASH,
                true
        ));

        // Add Registration
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_RECORD,
                REL_FONDS_STRUCTURE + RECORD + SLASH,
                true
        ));

        // Add DocumentDescription
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + DOCUMENT_DESCRIPTION,
                REL_FONDS_STRUCTURE + DOCUMENT_DESCRIPTION + SLASH,
                true
        ));

        // Add DocumentObject
        aPIDetails.add(new APIDetail(
                publicUrlPath + SLASH + HREF_BASE_DOCUMENT_OBJECT,
                REL_FONDS_STRUCTURE + DOCUMENT_OBJECT + SLASH,
                true
        ));
    }
}
