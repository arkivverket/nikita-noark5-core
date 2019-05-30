package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing RecordNote object as JSON.
 */
public class RecordNoteHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INikitaEntity noarkSystemIdEntity,
            HateoasNoarkObject recordNoteHateoas,
            JsonGenerator jgen) throws IOException {

        RecordNote recordNote = (RecordNote) noarkSystemIdEntity;

        jgen.writeStartObject();

        printSystemIdEntity(jgen, recordNote);
        printCreateEntity(jgen, recordNote);

        if (recordNote.getArchivedDate() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_DATE,
                    Serialize.formatDateTime(recordNote.getArchivedDate()));
        }
        if (recordNote.getArchivedBy() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_BY,
                    recordNote.getArchivedBy());
        }
        printDisposal(jgen, recordNote);
        printScreening(jgen, recordNote);
        printClassified(jgen, recordNote);

        // handle general Record  properties
        if (recordNote.getRecordId() != null) {
            jgen.writeStringField(RECORD_ID, recordNote.getRecordId());
        }
        if (recordNote.getTitle() != null) {
            jgen.writeStringField(TITLE, recordNote.getTitle());
        }
        if (recordNote.getOfficialTitle() != null) {
            jgen.writeStringField(FILE_PUBLIC_TITLE,
                    recordNote.getOfficialTitle());
        }
        if (recordNote.getDescription() != null) {
            jgen.writeStringField(DESCRIPTION, recordNote.getDescription());
        }
        printKeyword(jgen, recordNote);
        printDocumentMedium(jgen, recordNote);
        printStorageLocation(jgen, recordNote);
        printComment(jgen, recordNote);

        if (recordNote.getDocumentDate() != null) {
            jgen.writeStringField(REGISTRY_ENTRY_DOCUMENT_DATE,
                    Serialize.formatDate(recordNote.getDocumentDate()));
        }
        if (recordNote.getReceivedDate() != null) {
            jgen.writeStringField(REGISTRY_ENTRY_RECEIVED_DATE,
                    Serialize.formatDate(recordNote.getReceivedDate()));
        }
        if (recordNote.getSentDate() != null) {
            jgen.writeStringField(REGISTRY_ENTRY_SENT_DATE,
                    Serialize.formatDate(recordNote.getSentDate()));
        }
        if (recordNote.getDueDate() != null) {
            jgen.writeStringField(REGISTRY_ENTRY_DUE_DATE,
                    Serialize.formatDate(recordNote.getDueDate()));
        }
        if (recordNote.getFreedomAssessmentDate() != null) {
            jgen.writeStringField(REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE,
                    Serialize.formatDate(
                            recordNote.getFreedomAssessmentDate()));
        }
        if (recordNote.getNumberOfAttachments() != null) {
            jgen.writeNumberField(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS,
                    recordNote.getNumberOfAttachments());
        }
        if (recordNote.getLoanedDate() != null) {
            jgen.writeStringField(CASE_LOANED_DATE,
                    Serialize.formatDate(recordNote.getLoanedDate()));
        }
        if (recordNote.getLoanedTo() != null) {
            jgen.writeStringField(CASE_LOANED_TO, recordNote.getLoanedTo());
        }
        printDocumentFlow(jgen, recordNote);
        printHateoasLinks(jgen, recordNoteHateoas.getLinks(recordNote));
        jgen.writeEndObject();
    }
}
