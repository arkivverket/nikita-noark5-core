package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.SignOffMethod;

/**
 * Created by tsodring on 13/02/18.
 */

public interface ISignOffMethodService {

    MetadataHateoas createNewSignOffMethod(
            SignOffMethod SignOffMethod, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 SignOffMethod signOffMethod,
                                 String outgoingAddress);

    SignOffMethod generateDefaultSignOffMethod();
}
