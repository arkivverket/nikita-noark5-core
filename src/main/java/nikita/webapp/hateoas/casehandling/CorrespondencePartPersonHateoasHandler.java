package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add CorrespondencePartHateoas links with CorrespondencePart
 * specific information
 */
@Component("correspondencePartPersonHateoasHandler")
public class CorrespondencePartPersonHateoasHandler
        extends CorrespondencePartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(ISystemId entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
    }


}
