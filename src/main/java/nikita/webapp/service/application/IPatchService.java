package nikita.webapp.service.application;

import nikita.common.model.nikita.PatchObject;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IPatchService {
    Object handlePatch(@NotNull final UUID originalObjectId, PatchObject patchObject);
}
