package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IBSMService {
    Optional<BSMMetadata> findBSMByName(@NotNull String name);

    void validateBSMList(@NotNull List<BSMBase> referenceBSMBase);
}
