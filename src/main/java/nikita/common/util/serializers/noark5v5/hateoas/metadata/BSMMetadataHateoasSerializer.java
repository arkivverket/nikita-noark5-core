package nikita.common.util.serializers.noark5v5.hateoas.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class BSMMetadataHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject bSMMetadataHateoas, JsonGenerator jgen)
            throws IOException {
        BSMMetadata bsmMetadata = (BSMMetadata) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, bsmMetadata);
        print(jgen, NAME, bsmMetadata.getName());
        print(jgen, TYPE, bsmMetadata.getType());
        printNullable(jgen, OUTDATED, bsmMetadata.getOutdated());
        printNullable(jgen, DESCRIPTION, bsmMetadata.getDescription());
        printNullable(jgen, SOURCE, bsmMetadata.getSource());
        printHateoasLinks(jgen, bSMMetadataHateoas.getLinks(bsmMetadata));
        jgen.writeEndObject();
    }
}
