package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 13/02/18.
 */

public interface ISignOffMethodService {

    MetadataHateoas createNewSignOffMethod(
            SignOffMethod signOffMethod);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final SignOffMethod incomingSignOffMethod);

    SignOffMethod generateDefaultSignOffMethod();
}
