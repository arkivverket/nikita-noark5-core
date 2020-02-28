package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CASE_FILE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW;
import static nikita.common.config.N5ResourceMappings.PRECEDENCE;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY;

@JsonSerialize(using = APIDetailsSerializer.class)
@Component
public class CaseHandlingDetails extends APIDetails {


    @Value("${nikita.server.hateoas.publicAddress}")
    private String publicUrlPath;

    public CaseHandlingDetails() {
        create();
    }

    public CaseHandlingDetails(String publicUrlPath) {
        super();
        this.publicUrlPath = publicUrlPath;
        create();
    }

    private void create() {

        // Add support for caseFile object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_CASE_FILE,
                REL_CASE_HANDLING + CASE_FILE + SLASH,
                true
        ));

        // Add support for registryEntry object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_CASE_HANDLING + SLASH + REGISTRY_ENTRY,
                REL_CASE_HANDLING + REGISTRY_ENTRY + SLASH,
                true
        ));

        // Add support for DocumentFlow object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_CASE_HANDLING + SLASH + DOCUMENT_FLOW,
                REL_CASE_HANDLING_DOCUMENT_FLOW,
                true
        ));

        // Add support for Precedence object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_CASE_HANDLING + SLASH + PRECEDENCE,
                REL_CASE_HANDLING_PRECEDENCE,
                true
        ));
    }
}
