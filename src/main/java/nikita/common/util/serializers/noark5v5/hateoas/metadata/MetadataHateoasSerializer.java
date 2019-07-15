package nikita.common.util.serializers.noark5v5.hateoas.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing Metadata entity as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 */
public class MetadataHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    private static final Logger logger =
            LoggerFactory.getLogger(MetadataHateoasSerializer.class);

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject metadataHateoas,
                                     JsonGenerator jgen)
            throws IOException {

        if (!(noarkSystemIdEntity instanceof IMetadataEntity)) {
            String msg = "Internal error when serialising " +
                    noarkSystemIdEntity + ". Not castable to nikita.common" +
                    ".model.noark5.v5.interfaces.entities.IMetadataEntity";
            logger.error(msg);
            throw new NikitaException(msg);
        }

        IMetadataEntity metadataEntity = (IMetadataEntity) noarkSystemIdEntity;

        jgen.writeStartObject();
        if (metadataEntity.getCode() != null) {
            jgen.writeStringField(CODE, metadataEntity.getCode());
        }
        if (metadataEntity.getName() != null) {
            jgen.writeStringField(CODE_NAME, metadataEntity.getName());
        }
        if (metadataEntity.getComment() != null) {
            jgen.writeStringField(CODE_COMMENT, metadataEntity.getComment());
        }
        printHateoasLinks(jgen, metadataHateoas.getLinks(metadataEntity));
        jgen.writeEndObject();
    }
}
