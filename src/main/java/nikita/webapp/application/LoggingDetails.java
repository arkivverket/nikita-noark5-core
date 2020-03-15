package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.APIDetailsSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CHANGE_LOG;
import static nikita.common.config.N5ResourceMappings.EVENT_LOG;

@JsonSerialize(using = APIDetailsSerializer.class)
@Component
public class LoggingDetails extends APIDetails {


    @Value("${nikita.server.hateoas.publicAddress}")
    private String publicUrlPath;

    public LoggingDetails() {
        create();
    }

    public LoggingDetails(String publicUrlPath) {
        super();
        this.publicUrlPath = publicUrlPath;
        create();
    }

    private void create() {

        // Add support for ChangeLog object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_LOGGING + SLASH + CHANGE_LOG,
                REL_LOGGING + CHANGE_LOG + SLASH,
                true
        ));

        // Add support for EventLog object
        aPIDetails.add(new APIDetail(
                publicUrlPath + HREF_BASE_LOGGING + SLASH + EVENT_LOG,
                REL_LOGGING + EVENT_LOG + SLASH,
                true
        ));
    }
}
