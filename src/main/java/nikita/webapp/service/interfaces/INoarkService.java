package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

/**
 * Created by tsodring on 4/6/17.
 * Common description of how most Noark services will be implemented
 *
 * Seems to be this class is redundant and can be removed ...
 */
public interface INoarkService {
    INoarkEntity updateNoarkEntity(String systemId, Long version, INoarkEntity updatedEntity);
    INoarkEntity createNewNoarkEntity(INoarkEntity entity);

    INoarkEntity findBySystemId(String systemId);
}
