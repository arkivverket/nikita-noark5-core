package nikita.webapp.service.application;

import nikita.common.model.nikita.PatchObject;

import java.util.UUID;

public interface IPatchService {
    Object handlePatch(UUID originalObjectId, PatchObject patchObject);
}
