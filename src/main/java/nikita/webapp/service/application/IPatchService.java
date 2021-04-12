package nikita.webapp.service.application;

import nikita.common.model.nikita.PatchObject;

import java.util.UUID;

public interface IPatchService {
    void handlePatch(UUID originalObjectId, PatchObject patchObject);
}
