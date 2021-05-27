package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.hateoas.metadata.BSMMetadataHateoas;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.webapp.service.interfaces.INoarkService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IBSMMetadataService
        extends INoarkService {

    /**
     * Retrieve a BSMMetadata wrapped as a BSMMetadataHateoas object
     * identified by the given systemId.
     * <p>
     *
     * @param systemId UUID of the BSMMetadata object to retrieve
     * @return The BSMMetadataHateoas object
     */
    BSMMetadataHateoas find(@NotNull final UUID systemId);

    /**
     * Persist a BSMMetadata object to the database and wrap the
     * persistedobject as a BSMMetadataHateoas.
     * <p>
     *
     * @param bsmMetadata incoming BSMMetadata object
     * @return The persisted BSMMetadataHateoas object
     */
    BSMMetadataHateoas save(@NotNull final BSMMetadata bsmMetadata);

    /**
     * Undertake an update to an existing BSMMetadata object identified by
     * the given systemId. The update is done using a PATCH request
     * <p>
     *
     * @param systemId     UUID of the BSMMetadata to update
     * @param patchObjects List of operations to undertake
     * @return The updated BSMMetadata object wrapped as a BSMMetadataHateoas
     */
    BSMMetadataHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects);

    /**
     * Undertake an update to an existing BSMMetadata object identified by
     * the given systemId.
     * <p>
     *
     * @param systemId    UUID of the BSMMetadata to update
     * @param aLong
     * @param bsmMetadata incoming BSMMetadata object
     * @return The updated BSMMetadata object wrapped as a BSMMetadataHateoas
     */
    BSMMetadataHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long aLong,
            @NotNull final BSMMetadata bsmMetadata);

    /**
     * Delete the BSMMetadata object identified by the given systemId.
     * <p>
     *
     * @param systemId UUID of the BSMMetadata to delete
     */
    void deleteEntity(@NotNull final UUID systemId);

    /**
     * Retrieve all BSMMetadata wrapped as a BSMMetadataHateoas object
     * <p>
     *
     * @return The BSMMetadataHateoas object containing all BSMMetadata
     */
    BSMMetadataHateoas findAll();
}
