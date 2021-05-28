package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing RegistryEntry object as JSON.
 */

public class RegistryEntryExpansionHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject registryEntryHateoas,
            JsonGenerator jgen) throws IOException {
        RegistryEntry registryEntry = (RegistryEntry) noarkSystemIdEntity;
        jgen.writeStartObject();
        printRecordEntity(jgen, registryEntry);
        printRecordNoteEntity(jgen, registryEntry);
        printRegistryEntryEntity(jgen, registryEntry);
        printHateoasLinks(jgen, registryEntryHateoas.getLinks(registryEntry));
        jgen.writeEndObject();
    }
}
