package nikita.webapp.hateoas;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import org.springframework.stereotype.Component;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add PartHateoas links with Part
 * specific information
 */
@Component("partUnitHateoasHandler")
public class PartUnitHateoasHandler
        extends PartHateoasHandler {

    @Override
    public void addEntityLinksOnTemplate(INikitaEntity entity,
                                         IHateoasNoarkObject
                                                 hateoasNoarkObject) {
        super.addEntityLinksOnTemplate(entity, hateoasNoarkObject);
    }


}
