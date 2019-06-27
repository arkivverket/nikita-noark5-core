package nikita.webapp.odata;

import nikita.common.model.noark5.v5.NoarkEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NikitaObjectWorker {

    private final Logger logger =
            LoggerFactory.getLogger(NikitaObjectWorker.class);

    public void handleCreateReference(NoarkEntity fromEntity,
                                      NoarkEntity toEntity,
                                      String referenceType) {
        fromEntity.createReference(toEntity, referenceType);
    }
}
