package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.BasicRecord;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IBasicRecordService {

    // All READ operations
    List<BasicRecord> findAllBasicRecordByOwner();

    BasicRecord findBySystemId(String basicRecordSystemId);

    // All UPDATE operations
    BasicRecord handleUpdate(@NotNull String systemId, @NotNull Long version,
                             @NotNull BasicRecord incomingBasicRecord);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);
}
