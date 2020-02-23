package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing RecordNote object as JSON.
 */
public class RecordNoteHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject recordNoteHateoas,
            JsonGenerator jgen) throws IOException {

        RecordNote recordNote = (RecordNote) noarkSystemIdEntity;

        jgen.writeStartObject();
        printRecordEntity(jgen, recordNote);
        printRecordNoteEntity(jgen, recordNote);

        printHateoasLinks(jgen, recordNoteHateoas.getLinks(recordNote));
        jgen.writeEndObject();
    }
}
