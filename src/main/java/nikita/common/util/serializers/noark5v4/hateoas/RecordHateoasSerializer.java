package nikita.common.util.serializers.noark5v4.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v4.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing record object as JSON.
 */
public class RecordHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject recordHateoas,
                                     JsonGenerator jgen) throws IOException {

        Record record = (Record) noarkSystemIdEntity;

        jgen.writeStartObject();
        printSystemIdEntity(jgen, record);
        printCreateEntity(jgen, record);

        if (record.getArchivedDate() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_DATE,
                    Serialize.formatDateTime(record.getArchivedDate()));
        }
        if (record.getArchivedBy() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_BY, record.getArchivedBy());
        }
        if (record.getArchivedDate() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_DATE,
                    Serialize.formatDateTime(record.getArchivedDate()));
        }
        if (record.getArchivedBy() != null) {
            jgen.writeStringField(RECORD_ARCHIVED_BY, record.getArchivedBy());
        }
        if (record.getTitle() != null) {
            jgen.writeStringField(TITLE, record.getTitle());
        }
        if (record.getOfficialTitle() != null) {
            jgen.writeStringField(FILE_PUBLIC_TITLE, record.getOfficialTitle());
        }
        if (record.getDescription() != null) {
            jgen.writeStringField(DESCRIPTION, record.getDescription());
        }
        printDisposal(jgen, record);
        printScreening(jgen, record);
        printClassified(jgen, record);
        printKeyword(jgen, record);
        printDocumentMedium(jgen, record);
        printStorageLocation(jgen, record);
        printComment(jgen, record);
        // TODO: FIX THIS printCrossReference(jgen, record);
        printHateoasLinks(jgen, recordHateoas.getLinks(record));
        jgen.writeEndObject();
    }
}
