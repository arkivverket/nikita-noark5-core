package nikita.webapp.service.interfaces;

import nikita.common.model.nikita.PatchMerge;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface INoarkService {

    /**
     * Update a systemId object using a RFC 7396 approach
     * <p>
     *
     * @param systemId   The systemId of the object to update
     * @param patchMerge Values to change
     * @return The updated BSMMetadata object wrapped as a BSMMetadataHateoas
     */
    Object handleUpdateRfc7396(@NotNull final UUID systemId,
                               @NotNull PatchMerge patchMerge);
}
