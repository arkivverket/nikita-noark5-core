package nikita.common.model.noark5.v5.joins;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by tsodring on 5/10/17.
 */

@Embeddable
public class RecordDocumentDescriptionId implements Serializable {
    private UUID recordId;
    private UUID documentDescriptionId;

    private RecordDocumentDescriptionId() {
    }

    public RecordDocumentDescriptionId(UUID recordId, UUID documentDescriptionId) {
        this.recordId = recordId;
        this.documentDescriptionId = documentDescriptionId;
    }

    public UUID getRecordId() {
        return recordId;
    }

    public UUID getDocumentDescriptionId() {
        return documentDescriptionId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof RecordDocumentDescriptionId))
            return false;
        RecordDocumentDescriptionId otherId = (RecordDocumentDescriptionId) other;
        return Objects.equals(recordId, otherId.recordId) &&
                Objects.equals(documentDescriptionId, otherId.documentDescriptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, documentDescriptionId);
    }
}
