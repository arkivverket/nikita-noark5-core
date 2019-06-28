package nikita.common.model.noark5.v5.joins;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by tsodring on 5/10/17.
 *
 * This table is an attempt to do the many to many as two onetomany as this is
 * meant to be more efficient. We need to decide if we will continue with the
 * approach or just delete this entity from the code base.
 *
 * This is not in use, it's just in the codebase as an example.
 */
@Entity
@Table(name = "test_record_document_description")
public class RecordDocumentDescription {
    @EmbeddedId
    private RecordDocumentDescriptionId id;
    @ManyToOne
    @MapsId("recordId")
    private Record record;
    @ManyToOne
    @MapsId("documentDescriptionId")
    private DocumentDescription documentDescription;

    private RecordDocumentDescription() {
    }

    public RecordDocumentDescription(Record record, DocumentDescription documentDescription) {
        this.record = record;
        this.documentDescription = documentDescription;
        this.id = new RecordDocumentDescriptionId(
                UUID.fromString(record.getSystemId()),
                UUID.fromString(documentDescription.getSystemId()));
    }

    //Getters and setters omitted for brevity
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof RecordDocumentDescription))
            return false;
        RecordDocumentDescription desc = (RecordDocumentDescription) other;
        return Objects.equals(record, desc.record) &&
                Objects.equals(documentDescription, desc.documentDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(record, documentDescription);
    }
}

