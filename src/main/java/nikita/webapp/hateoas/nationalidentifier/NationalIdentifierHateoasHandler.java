package nikita.webapp.hateoas.nationalidentifier;

import nikita.webapp.hateoas.SystemIdHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import org.springframework.stereotype.Component;

/**
 * Used to add NationalIdentifierHateoas links with NationalIdentifier
 * specific information
 **/
@Component("nationalIdentifierHateoasHandler")
public class NationalIdentifierHateoasHandler
        extends SystemIdHateoasHandler
        implements INationalIdentifierHateoasHandler {
}
