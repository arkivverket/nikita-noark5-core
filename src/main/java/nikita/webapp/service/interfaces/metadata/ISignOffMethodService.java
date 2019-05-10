package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.SignOffMethod;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 13/02/18.
 */

public interface ISignOffMethodService {

    MetadataHateoas createNewSignOffMethod(
            SignOffMethod signOffMethod);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final SignOffMethod incomingSignOffMethod);

    SignOffMethod generateDefaultSignOffMethod();
}
