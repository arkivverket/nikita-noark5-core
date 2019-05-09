package nikita.webapp.hateoas.casehandling;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
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
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject,
                                         String outgoingAddress) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject,
                outgoingAddress);
    }


}
