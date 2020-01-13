package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ICorrespondencePartTypeService
    extends IMetadataSuperService {

    CorrespondencePartType createNewCorrespondencePartType(
            CorrespondencePartType correspondencePartType);

    List<IMetadataEntity> findAll();

    CorrespondencePartType update(CorrespondencePartType correspondencePartType);

    void deleteEntity(@NotNull String correspondencePartTypeCode);
}
