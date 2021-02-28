package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.hateoas.metadata.BSMMetadataHateoas;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.webapp.service.interfaces.INoarkService;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IBSMMetadataService
        extends INoarkService {

    /**
     * Retrieve a BSMMetadata wrapped as a BSMMetadataHateoas object
     * identified by the given systemID.
     * <p>
     *
     * @param systemID UUID of the BSMMetadata object to retrieve
     * @return The BSMMetadataHateoas object
     */
    ResponseEntity<BSMMetadataHateoas> find(@NotNull UUID systemID);

    /**
     * Persist a BSMMetadata object to the database and wrap the
     * persistedobject as a BSMMetadataHateoas.
     * <p>
     *
     * @param bsmMetadata incoming BSMMetadata object
     * @return The persisted BSMMetadataHateoas object
     */
    ResponseEntity<BSMMetadataHateoas> save(@NotNull BSMMetadata bsmMetadata);

    /**
     * Undertake an update to an existing BSMMetadata object identified by
     * the given systemID. The update is done using a PATCH request
     * <p>
     *
     * @param systemID     UUID of the BSMMetadata to update
     * @param patchObjects List of operations to undertake
     * @return The updated BSMMetadata object wrapped as a BSMMetadataHateoas
     */
    ResponseEntity<BSMMetadataHateoas> handleUpdate(
            @NotNull UUID systemID, @NotNull PatchObjects patchObjects);

    /**
     * Delete the BSMMetadata object identified by the given systemID.
     * <p>
     *
     * @param systemId UUID of the BSMMetadata to delete
     */
    void deleteEntity(@NotNull UUID systemId);

    /**
     * Retrieve all BSMMetadata wrapped as a BSMMetadataHateoas object
     * <p>
     *
     * @return The BSMMetadataHateoas object containing all BSMMetadata
     */
    ResponseEntity<BSMMetadataHateoas> findAll();
}
