package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing record object as JSON.
 */
public class RecordHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject recordHateoas,
                                     JsonGenerator jgen) throws IOException {

        Record record = (Record) noarkSystemIdEntity;

        jgen.writeStartObject();
        printRecordEntity(jgen, record);

        if (record instanceof RegistryEntry) {
            printRecordNoteEntity(jgen, (RegistryEntry) record);
            printRegistryEntryEntity(jgen, (RegistryEntry) record);
        } else if (record instanceof RecordNote) {
            printRecordNoteEntity(jgen, (RecordNote) record);
        }

        printDisposal(jgen, record);
        printScreening(jgen, record);
        printClassified(jgen, record);
        printKeyword(jgen, record);
        printDocumentMedium(jgen, record);
        printStorageLocation(jgen, record);
        // TODO: FIX THIS printCrossReference(jgen, record);
        if (record instanceof RegistryEntry) {
            printElectronicSignature(jgen, (RegistryEntry) record);

        } else if (record instanceof RecordNote) {
        }

        printHateoasLinks(jgen, recordHateoas.getLinks(record));
        jgen.writeEndObject();
    }
}
