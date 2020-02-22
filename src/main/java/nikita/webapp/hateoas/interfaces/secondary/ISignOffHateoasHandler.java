package nikita.webapp.hateoas.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISignOffEntity;
import nikita.webapp.hateoas.interfaces.ISystemIdHateoasHandler;

public interface ISignOffHateoasHandler
        extends ISystemIdHateoasHandler {

    void addRegistryEntry(ISignOffEntity signOff,
                          IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSignedOffRegistryEntry
        (ISignOffEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addReferenceSignedOffCorrespondenceParty
        (ISignOffEntity entity, IHateoasNoarkObject hateoasNoarkObject);

    void addSignOffMethod(ISignOffEntity signOff,
                          IHateoasNoarkObject hateoasNoarkObject);
}
