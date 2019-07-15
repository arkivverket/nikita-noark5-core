package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ICorrespondencePartTypeService {

    CorrespondencePartType createNewCorrespondencePartType(
            CorrespondencePartType correspondencePartType);

    Iterable<CorrespondencePartType> findAll();

    CorrespondencePartType update(CorrespondencePartType correspondencePartType);

    CorrespondencePartType findByCode(String code);

    void deleteEntity(@NotNull String correspondencePartTypeCode);
}
